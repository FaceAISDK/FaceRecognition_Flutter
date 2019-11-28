package com.anylife.module_main.business.navigation;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.anylife.module_main.R;
import com.anylife.module_main.business.navigation.fragment.MainFragment;
import com.anylife.module_main.business.navigation.fragment.MeFragment;
import com.anylife.module_main.blog.ui.BlogListFragment;
import com.anylife.module_main.business.navigation.fragment.NewsFragmentShell;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zlb.Sp.SPDao;
import com.zlb.base.BaseDaggerActivity;
import com.zlb.httplib.BuildConfig;
import com.zlb.httplib.dialog.HttpUiTips;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;

/**
 * APP的主控区域，跳转和分发集中在这里
 * <p>
 * 组件化工程 butterKnife 不能使用，
 * 用 https://plugins.jetbrains.com/plugin/8219-android-view-generator 代替吧
 * <p>
 * <p>
 * 应用架构指南：https://developer.android.google.cn/jetpack/docs/guide
 * <p>
 * Created by anylife.zlb@gmail.com on 2019/2/15.
 */
public class MainActivityBottomNavi extends BaseDaggerActivity {
    private ViewPager viewPager;
    //    private MenuItem menuItem;
    private BottomNavigationView navigation;

    @Inject
    SPDao spDao;

    @Inject
    NewsFragmentShell newsFragmentShell;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main_bottom_navi;
    }

    public void initViews() {
        setActivityTitle("主页");
        //设置App 的Logo
        getToolbar().setNavigationIcon(null);
        HttpUiTips.showDialog(this, "");

        viewPager = findViewById(R.id.viewpager);
        //这里的icon 一般都有动画的
        navigation = findViewById(R.id.navigation);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }

        navigation.setOnNavigationItemSelectedListener(menuItem -> {
            viewPager.setCurrentItem(menuItem.getOrder(),false);
            setActivityTitle(menuItem.getTitle());
            return true;
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MenuItem menuItem = navigation.getMenu().getItem(position);
                menuItem.setChecked(true);
                setActivityTitle(menuItem.getTitle());
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
        adapter.addFragment(BlogListFragment.newInstance());

        if (BuildConfig.isModule) {
            //组件化开发模式这里添加的空的 newsFragmentShell
            adapter.addFragment(newsFragmentShell);
        } else {
            //Fragment 单独组件化出去了
            adapter.addFragment((Fragment) ARouter.getInstance().build("/news/packageFragment").navigation());
        }

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

    @Override
    public boolean isShowBackIcon() {
        return false;
    }


}
