
package com.zenglb.framework.news;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.zlb.base.BaseWebViewActivity;

/**
 * WIP :work in Process
 * <p>
 * 验证 JSBridge
 */
@Route(path = "/news/webActivity")
public class NewsWebActivity extends BaseWebViewActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_webview;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url = getIntent().getStringExtra(BaseWebViewActivity.URL);
        setURL(url);
    }

}

