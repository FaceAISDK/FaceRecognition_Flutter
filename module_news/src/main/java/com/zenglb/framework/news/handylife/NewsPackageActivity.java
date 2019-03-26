package com.zenglb.framework.news.handylife;

import android.os.Bundle;
import android.view.View;
import com.zenglb.framework.goodlife.R;
import com.zlb.base.BaseActivity;
import com.zlb.base.BaseMVPActivity;
import component.android.com.component_base.ComponentServiceFactory;
import component.android.com.component_base.base.IFragmentService;


/**
 * Module_news 模块调试的时候包装NewsFragment
 *
 * NewsFragment 实际上是在MainBotNavi 中被使用
 *
 */
public class NewsPackageActivity extends BaseMVPActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolBarVisible(View.GONE);  //这里是不需要Toolbar
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_package;
    }

    @Override
    protected void initViews() {

        IFragmentService iFragmentService = ComponentServiceFactory.getInstance(this).getNewsFragmentService();

        //iFragmentService 是空的话说明是组件化的单独的调试啊
        if (null != iFragmentService) {
            iFragmentService.newFragment(this, R.id.fragment_container, getSupportFragmentManager(), null, null);
        }

    }
}
