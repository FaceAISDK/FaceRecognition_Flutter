package com.zenglb.framework.news.news;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayout;
import com.zenglb.framework.news.R;
import com.zenglb.framework.news.http.result.AppBean;
import com.zenglb.framework.news.http.result.HotNewsResult;
import com.zlb.statelivedata.StateData;
import com.zlb.base.BaseStatusFragment;
import com.zlb.commontips.TimeoutCallback;
import com.zlb.httplib.dialog.HttpUiTips;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * 新闻的管理包装Fragment ，里面是TabLayout 包装了N个NewsFragment
 * <p>
 * 为了组件化。
 */
@Route(path = "/news/packageFragment")
public class NewsPackageFragment extends BaseStatusFragment implements TabLayout.OnTabSelectedListener{
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private NewsViewModel newsViewModel;

    /**
     * ARouter 提供实例化了，暂时没有用
     *
     * @return
     */
    public static NewsPackageFragment newInstance() {
        NewsPackageFragment fragment = new NewsPackageFragment();
        Bundle args = new Bundle();

        // 构建标准的路由请求，startActivityForResult
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    protected int onCreateFragmentView() {
        return R.layout.fragment_news_package;
    }


    @Override
    public void initView(View rootView) {
        // Set up the ViewPager with the sections adapter.
        mViewPager = rootView.findViewById(R.id.container);

        mTabLayout = rootView.findViewById(R.id.layout_tab);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);       //Set Mode

        mTabLayout.addOnTabSelectedListener(this);

        //use Dagger
        newsViewModel = ViewModelProviders.of(this, viewModelFactory).get(NewsViewModel.class);

        //ViewModel 最终消亡是在 Activity 被销毁的时候，会执行它的onCleared()进行数据的清理。
        //去获取博客数据
        getNews();
    }

    /**
     * 重写请求重试
     *
     * @param v
     */
    @Override
    protected void onHttpReload(View v) {
        newsViewModel.getNews();
    }


    private void disposeData(HotNewsResult hotNewsResult){
        List<String> tabsTitle = new ArrayList<>();

        Field[] fields = hotNewsResult.getClass().getDeclaredFields();
        for (Field field : fields) {
            //匹配是否为静态常量
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            tabsTitle.add(field.getName());
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager(),
                hotNewsResult, tabsTitle);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //set mTabLayout up With mViewPager
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(tabsTitle.size() - 1);
    }


    /**
     * 去获取数据
     *
     */
    public void getNews() {

        newsViewModel.getNews().observe(this, new Observer<StateData<HotNewsResult>>() {
            @Override
            public void onChanged(StateData<HotNewsResult> hotNewsResultStateData) {
                switch (hotNewsResultStateData.getStatus()) {
                    case SUCCESS:
                        disposeData(hotNewsResultStateData.getData());
                        HttpUiTips.dismissDialog(getActivity());
                        break;
                    case ERROR:
                        //这里可以根据ErrorCode进行封装统一处理
                        mBaseLoadService.showCallback(TimeoutCallback.class);//其他回调
                        HttpUiTips.dismissDialog(getActivity());
                        break;
                    case LOADING:
                        HttpUiTips.showDialog(getActivity(),"请求中");
                        Log.e("TTT","显示Loading UI ");
                        break;
                }
            }
        });
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    /**
     * FragmentStatePagerAdapter ：不可见会被回收资源，也就是占用的资源少啊
     * FragmentPagerAdapter      ：可能会导致大量的内存被占用，资源一直占用 （可以配合懒加载）
     */
    private class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private List<String> tabsTitle;
        private HotNewsResult articlesResult;

        public SectionsPagerAdapter(FragmentManager fm, HotNewsResult articlesResult, List<String> tabsTitle) {
            super(fm);
            this.articlesResult = articlesResult;
            this.tabsTitle = tabsTitle;
        }

        /**
         * 根据反射获取值
         *
         * @param fieldName
         * @return
         */
        public Object getValue(String fieldName) {
            try {
                Field field = articlesResult.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(articlesResult);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        public Fragment getItem(int i) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a TabsFragment that load tabsDataTypeTitle[position] type data
            return NewsFragment.newInstance(tabsTitle.get(i), (ArrayList<AppBean>)getValue(tabsTitle.get(i)));
        }

        @Override
        public int getCount() {
            return tabsTitle.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabsTitle.get(position);
        }

    }


}
