package com.anylife.module_main.business.navigation;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anylife.module_main.R;
import com.anylife.module_main.business.navigation.fragment.MainFragment;
import com.anylife.module_main.business.navigation.fragment.MeFragment;
import com.anylife.module_main.business.navigation.fragment.VideoListFragment;
import com.anylife.module_main.business.navigation.fragment.NewsFragmentShell;
import com.zenglb.downloadinstaller.DownloadInstaller;
import com.zenglb.downloadinstaller.DownloadProgressCallBack;
import com.zlb.Sp.SPDao;
import com.zlb.base.BaseMVPActivity;

import javax.inject.Inject;

import component.android.com.component_base.ComponentServiceFactory;
import component.android.com.component_base.base.IFragmentService;
import es.dmoral.toasty.Toasty;

/**
 * APP的主控区域，跳转和分发集中在这里
 *
 * 组件化工程 butterKnife 不能使用，
 * 用https://plugins.jetbrains.com/plugin/8219-android-view-generator 代替吧
 *
 * 
 * Created by anylife.zlb@gmail.com on 2019/2/15.
 */
public class MainActivityBottomNavi extends BaseMVPActivity {
    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView navigation;

    @Inject
    SPDao spDao;


    //懒加载一下子
    @Inject
    NewsFragmentShell newsFragmentShell;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int i = item.getItemId();
            if (i == R.id.navigation_home) {
                viewPager.setCurrentItem(0);
                setToolBarTitle("主页");
                return true;
            } else if (i == R.id.navigation_dashboard) {
                viewPager.setCurrentItem(1);
                setToolBarTitle("新闻资讯");
                return true;
            } else if (i == R.id.navigation_notifications) {
                viewPager.setCurrentItem(2);
                setToolBarTitle("私密记事");
                return true;
            } else if (i == R.id.navigation_set) {
                viewPager.setCurrentItem(3);
                setToolBarTitle("设置");
                return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolBarTitle("主页");
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_bottom_navi;
    }



    public void initViews() {
        //设置App 的Logo
        getToolbar().setNavigationIcon(R.drawable.ic_av_timer);

        //这里的icon 一般都有动画的
        navigation = findViewById(R.id.navigation);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);

            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager = findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                menuItem = navigation.getMenu().getItem(position);
                menuItem.setChecked(true);
                //Title 没有改变
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });

        viewPager.setOnTouchListener((v, event) -> false); //禁止ViewPager滑动
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
    }




    /**
     * 根据业务划分组件化
     *
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(MainFragment.newInstance());

        //特殊的处理一下newsFragment。因为单独组件化出去了，团队专人负责，单独调试编译
        IFragmentService iFragmentService = ComponentServiceFactory.getInstance(this).getNewsFragmentService();
        //iFragmentService 是空的话说明是组件化的单独的调试啊
        if (null != iFragmentService) {
            adapter.addFragment(iFragmentService.getFragment(null));
        } else {
            adapter.addFragment(newsFragmentShell);
        }

        adapter.addFragment(VideoListFragment.newInstance());
        adapter.addFragment(MeFragment.newInstance());

        viewPager.setAdapter(adapter);
    }



    /**
     * 快速按2次退出
     */
    private long exitTime = 0;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime < 2000) {
                finish();
            } else {
                exitTime = System.currentTimeMillis();
                Toasty.info(this.getApplicationContext(), "再按一次退出！", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else {
            return super.dispatchKeyEvent(event); // 按下其他按钮，调用父类进行默认处理
        }
    }

    /**
     * 主页面不要显示返回按钮
     *
     * @return
     */
    protected boolean isShowBacking() {
        return false;
    }


}
