package com.zenglb.framework.modulea.navigation;

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

import com.zenglb.framework.modulea.R;
import com.zlb.base.BaseMVPActivity;
import com.zenglb.framework.modulea.mvp.handylife.AnyLifeFragment;
import com.zenglb.framework.modulea.navigation.fragment.DemoFragment;
import com.zlb.Sp.SPDao;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;

/**
 * 这里应该就是一个App 的主控区域，是灵魂吧，优化主要是这里
 * <p>
 * 本来挺好的，但是4/5 个bottom navi 的时候 不能定制样式啊，反射XX
 * Sadly, there isn't any way to force enable or disable this behaviour which may not work with every design.
 * It also doesn't allow populating the Bottom Navigation View with more than five items - as per the design spec
 * (it throws an IllegalArgumentException if you try to).
 * <p>
 * Created by anylife.zlb@gmail.com on 2017/3/24.
 */
public class MainActivityBottomNavi extends BaseMVPActivity {
    private ViewPager viewPager;
    private MenuItem menuItem;

    @Inject
    SPDao spDao;

    @Inject
    DemoFragment demoFragment;  // Lazy<DemoFragment>

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            int i = item.getItemId();

            if (i == R.id.navigation_home) {
                viewPager.setCurrentItem(0);
                setToolBarTitle("组件化架构with dagger,rxjava ...");
                return true;
            } else if (i == R.id.navigation_dashboard) {
                viewPager.setCurrentItem(1);
                setToolBarTitle("I will be confirm");
                return true;
            } else if (i == R.id.navigation_notifications) {
                viewPager.setCurrentItem(2);
                setToolBarTitle("消息");
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
        setToolBarTitle("组件化架构with dagger,rxjava ...");
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
        getToolbar().setNavigationIcon(null); //没有返回按钮

        final BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
//        navigation.setAccessibilityLiveRegion(BottomNavigationView.ACCESSIBILITY_LIVE_REGION_ASSERTIVE);

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
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

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
            public void onPageScrollStateChanged(int state) {}
        });

        viewPager.setOnTouchListener((v, event) -> false); //禁止ViewPager滑动
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(demoFragment);
        adapter.addFragment(AnyLifeFragment.newInstance("eat"));
        adapter.addFragment(AnyLifeFragment.newInstance("shop"));
        adapter.addFragment(AnyLifeFragment.newInstance("cityguide"));
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
