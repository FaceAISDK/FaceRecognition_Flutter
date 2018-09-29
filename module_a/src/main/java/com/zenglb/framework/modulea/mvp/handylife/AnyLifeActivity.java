package com.zenglb.framework.modulea.mvp.handylife;

import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import com.zenglb.framework.modulea.R;
import com.zlb.base.BaseMVPActivity;

/**
 * HandyLifeActivity 主要Activity。这里的UI 就不做测试了
 * <p>
 * 如果很熟悉，那应该就是了吧 ！
 * <p>
 * Created by zlb on 2018/3/23.
 */
public class AnyLifeActivity extends BaseMVPActivity implements TabLayout.OnTabSelectedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.layout_tab);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);       //Set Mode

        mTabLayout.addTab(mTabLayout.newTab().setText(tabsTitle[0]));
        mTabLayout.addTab(mTabLayout.newTab().setText(tabsTitle[1]));
        mTabLayout.addTab(mTabLayout.newTab().setText(tabsTitle[2]));

        mTabLayout.addOnTabSelectedListener(this);

        //set mTabLayout up With mViewPager
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(tabsTitle.length - 1);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_any_life;
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
     * A FragmentPagerAdapter that returns a fragment load tabsDataTypeTitle[position] type data
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a TabsFragment that load tabsDataTypeTitle[position] type data
            return AnyLifeFragment.newInstance(tabsDataTypeTitle[position]);
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
