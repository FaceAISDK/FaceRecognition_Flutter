package com.anylife.module_main.business.navigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
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
import com.zlb.Sp.SPDao;
import com.zlb.base.BaseMVPActivity;

import javax.inject.Inject;

import component.android.com.component_base.ComponentServiceFactory;
import component.android.com.component_base.base.IFragmentService;
import es.dmoral.toasty.Toasty;

/**
 * APP的主控区域，跳转和分发集中在这里
 * <p>
 * <p>
 * butterKnife 不能使用，用https://plugins.jetbrains.com/plugin/8219-android-view-generator 代替吧
 * <p>
 * 官方的BottomNavi
 * <p>
 * Sadly, there isn't any way to force enable or disable this behaviour which may not work with every design.
 * It also doesn't allow populating the Bottom Navigation View with more than five items - as per the design spec
 * (it throws an IllegalArgumentException if you try to).
 * <p>
 * Created by anylife.zlb@gmail.com on 2019/2/15.
 */
public class MainActivityBottomNavi extends BaseMVPActivity {
    private ViewPager viewPager;
    private MenuItem menuItem;

    @Inject
    SPDao spDao;

    @Inject
    NewsFragmentShell newsFragmentShell;  // Lazy<DemoFragment>

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
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_bottom_navi;
    }


    public void initViews() {
        getToolbar().setNavigationIcon(R.drawable.ic_av_timer);

        final BottomNavigationView navigation = findViewById(R.id.navigation);

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
     *
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(MainFragment.newInstance());


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

    protected boolean isShowBacking() {
        return false;
    }

}
