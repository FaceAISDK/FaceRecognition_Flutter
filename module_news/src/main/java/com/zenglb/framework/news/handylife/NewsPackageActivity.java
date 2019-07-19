package com.zenglb.framework.news.handylife;

import android.os.Bundle;
import android.view.View;
import com.zenglb.framework.news.R;
import com.zlb.base.BaseMVPActivity;

import javax.inject.Inject;



/**
 *  仅仅是调试的时候使用，没有实际的用途
 *
 *
 */
public class NewsPackageActivity extends BaseMVPActivity {

    @Inject
    String testNameStr;


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

    }
}
