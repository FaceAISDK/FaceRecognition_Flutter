package com.zenglb.framework.news.handylife;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayout;
import com.zenglb.framework.news.R;
import com.zenglb.framework.news.http.result.ArticlesResult;
import com.zenglb.framework.news.http.result.NewsBean;
import com.zlb.base.BaseStatusFragment;

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
public class NewsPackageFragment extends BaseStatusFragment implements TabLayout.OnTabSelectedListener, NewsContract.NewsView {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Inject
    NewsPresenter mPresenter;  //dagger

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
        mPresenter.getHandyLifeData();

        // Set up the ViewPager with the sections adapter.
        mViewPager = rootView.findViewById(R.id.container);

        mTabLayout = rootView.findViewById(R.id.layout_tab);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);       //Set Mode

        mTabLayout.addOnTabSelectedListener(this);
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
     * 显示数据.
     *
     * @param articlesResult
     */
    @Override
    public void showHandyLifeData(ArticlesResult articlesResult) {
        List<String> tabsTitle = new ArrayList<>();

        Field[] fields = articlesResult.getClass().getDeclaredFields();
        for (Field field : fields) {
            //匹配是否为静态常量
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            tabsTitle.add(field.getName());
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager(), articlesResult, tabsTitle);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //set mTabLayout up With mViewPager
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.setOffscreenPageLimit(tabsTitle.size() - 1);
    }


    /**
     * get Http Data failed.
     *
     * @param code
     * @param message
     */
    @Override
    public void getHandyLifeDataFailed(int code, String message) {

        //弹出错误的信息出来
    }

    /**
     * FragmentStatePagerAdapter ：不可见会被回收资源，也就是占用的资源少啊
     * FragmentPagerAdapter      ：可能会导致大量的内存被占用，资源一直占用 （可以配合懒加载）
     */
    private class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private List<String> tabsTitle;
        private ArticlesResult articlesResult;

        public SectionsPagerAdapter(FragmentManager fm, ArticlesResult articlesResult, List<String> tabsTitle) {
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
            return NewsFragment.newInstance(tabsTitle.get(i), (ArrayList<NewsBean>)getValue(tabsTitle.get(i)));
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


    @Override
    public void onResume() {
        super.onResume();
        //Bind view to the presenter
        mPresenter.takeView(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }


}
