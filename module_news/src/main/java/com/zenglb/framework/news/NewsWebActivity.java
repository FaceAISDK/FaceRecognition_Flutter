
package com.zenglb.framework.news;

import android.os.Bundle;
import com.zlb.base.BaseWebViewActivity;

/**
 * WIP :work in Process
 * <p>
 * 验证 JSBridge
 */
public class NewsWebActivity extends BaseWebViewActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_webview;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url = getIntent().getStringExtra(BaseWebViewActivity.URL);
        setURL(url);

    }



}

