
package com.zenglb.framework.goodlife;

import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.zlb.base.BaseWebViewActivity;

/**
 * WIP :work in Process
 * <p>
 * 验证 JSBridge
 */

public class GoodLifeWebActivity extends BaseWebViewActivity {
    private final static int CAMERA_PERMISSION_CODE = 1000;    //请求CAMERA权限的Code
    private final static int REQUEST_CAPTURE_IMG = 1001;    //相册选取

    private AdView mAdView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goodlife_webview;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url = getIntent().getStringExtra(BaseWebViewActivity.URL);
        setURL(url);


        MobileAds.initialize(this, "ca-app-pub-8621230724267558~7770389405");


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }


}

