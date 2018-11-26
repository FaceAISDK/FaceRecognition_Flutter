package com.zenglb.framework.goodlife.handylife;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.zenglb.framework.goodlife.GoodLifeWebActivity;
import com.zenglb.framework.goodlife.R;
import com.zlb.base.BaseMVPActivity;
import com.zlb.base.BaseWebViewActivity;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;

/**
 * GoodLifeActivity 主要Activity。
 * <p>
 * 【关于华东冲突的处理】
 * <p>
 * SwipeRefreshLayout和ViewPager 滑动冲突处理
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * Created by zlb on 2018/3/23.
 */
public class GoodLifeActivity extends BaseMVPActivity implements TabLayout.OnTabSelectedListener {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;       //TabLayout
    private String[] tabsTitle;         //tabs 的标题
    private String[] tabsDataTypeTitle; //每个Tabs 请求Http 数据时候对应的类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolBarVisible(View.GONE);  //这里是不需要Toolbar 的
    }


    @Override
    protected void initViews() {
        //init basic data
        tabsTitle = getResources().getStringArray(R.array.tabs_title_array);
        tabsDataTypeTitle = getResources().getStringArray(R.array.tabs_data_type_array);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("WhatApp-1");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = findViewById(R.id.layout_tab);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);       //Set Mode

        mTabLayout.addOnTabSelectedListener(this);

        //set mTabLayout up With mViewPager
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(tabsTitle.length - 1);


//        //添加页面滑动监听
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//                if (i == SCROLL_STATE_DRAGGING) {
//                    mSwipeRefreshLayout.setEnabled(false);//设置不可触发
//                } else if (i == SCROLL_STATE_SETTLING) {
//                    mSwipeRefreshLayout.setEnabled(true);//设置可触发
//                }
//            }
//        });

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_good_life;
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_good_life, menu);
        return true;
    }


    //定义菜单响应事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_author:
                Toast.makeText(this, "刀爷在这里走丢了 ...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_more:
                Intent intent = new Intent(getContext(), GoodLifeWebActivity.class);
                intent.putExtra(BaseWebViewActivity.URL, "https://github.com/AnyLifeZLB");

                startActivity(intent);
                break;
        }
        return true;
    }


    /**
     * FragmentStatePagerAdapter ：不可见会被回收资源，也就是占用的资源少啊
     * FragmentPagerAdapter      ：可能会导致大量的内存被占用，资源一直占用
     *
     *
     */
    private class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a TabsFragment that load tabsDataTypeTitle[position] type data
            return GoodLifeFragment.newInstance(tabsDataTypeTitle[position]);
        }

        @Override
        public int getCount() {
            return tabsTitle.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabsTitle[position];
        }
    }

}
