package com.zenglb.framework.news.handylife;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.zenglb.framework.news.R;


/**
 * 新闻的管理包装Fragment ，里面是TabLayout 包装了N个NewsFragment
 *
 * 为了组件化。
 *
 */
//@ActivityScope
public class NewsPackageFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private String[] tabsTitle;
    private String[] tabsDataTypeTitle;


    public static NewsPackageFragment newInstance() {
        NewsPackageFragment fragment = new NewsPackageFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_package,container, false);

        initView(rootView);
        return rootView;
    }

    
    public void initView(View rootView) {
        //init basic data
        tabsTitle = getResources().getStringArray(R.array.tabs_title_array);
        tabsDataTypeTitle = getResources().getStringArray(R.array.tabs_data_type_array);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = rootView.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = rootView.findViewById(R.id.layout_tab);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);       //Set Mode

        mTabLayout.addOnTabSelectedListener(this);

        //set mTabLayout up With mViewPager
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(tabsTitle.length - 1);
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
            return NewsFragment.newInstance(tabsDataTypeTitle[position]);
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
