package com.zenglb.framework.news.FragmentService;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zenglb.framework.news.handylife.NewsPackageFragment;

import component.android.com.component_base.base.IFragmentService;


/**
 * 主页面的NewsFragment独立性很强，拆分到这里后NewsFragmentService 让Fragment 在壳app初始化的
 * 时候就能init 并注入到{MainActivityBottomNavi}
 *
 */
public class NewsFragmentService implements IFragmentService {

    //获取目标的fragment来进行操作
    @Override
    public Fragment getFragment(String tag) {
        return NewsPackageFragment.newInstance();
    }


    //用于固定的区域来填充相应fragment
    @Override
    public void newFragment(Activity activity, int resId, FragmentManager fragmentManager, Bundle bundle, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(resId, NewsPackageFragment.newInstance(), tag);
        transaction.commit();
    }


}
