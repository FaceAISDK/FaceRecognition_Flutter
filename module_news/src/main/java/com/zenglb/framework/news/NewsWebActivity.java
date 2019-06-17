
package com.zenglb.framework.news;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.zenglb.framework.news.DynamicProxy.Sell;
import com.zenglb.framework.news.DynamicProxy.Vendor;
import com.zenglb.framework.news.R;
import com.zlb.base.BaseWebViewActivity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * WIP :work in Process
 * <p>
 * 验证 JSBridge
 */
public class NewsWebActivity extends BaseWebViewActivity {

    private AdView mAdView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_webview;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url = getIntent().getStringExtra(BaseWebViewActivity.URL);
        setURL(url);

        //MobileAds
        MobileAds.initialize(this, "ca-app-pub-8621230724267558~7770389405");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



    }


}

