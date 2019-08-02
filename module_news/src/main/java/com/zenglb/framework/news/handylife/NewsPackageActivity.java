package com.zenglb.framework.news.handylife;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zenglb.framework.news.R;
import com.zlb.base.BaseDaggerActivity;

import javax.inject.Inject;


/**
 * 仅仅是调试的时候使用，没有实际的用途
 *
 * (Fragment) ARouter.getInstance().build("/news/packageFragment").navigation()
 * 被组件化到了Main
 */
public class NewsPackageActivity extends BaseDaggerActivity {

    @Inject
    String testNameStr;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setToolBarTitle("News 模块组件化调试");
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_package;
    }

    @Override
    protected void initViews() {
        setToolBarTitle("News 模块组件化调试");

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container,(Fragment) ARouter.getInstance().build("/news/packageFragment").navigation())
                .commit();
    }

    @Override
    protected boolean isShowBackIcon() {
        return false;
    }

}
