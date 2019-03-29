package debug;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.zenglb.framework.news.R;
import com.zenglb.framework.news.handylife.NewsPackageActivity;
import com.zlb.Sp.SPDao;
import javax.inject.Inject;

import es.dmoral.toasty.Toasty;

import static com.google.android.gms.ads.AdRequest.ERROR_CODE_INTERNAL_ERROR;

/**
 * 单独的Module 也需要Launcher
 *
 */
public class NewsLauncherActivity extends AppCompatActivity {

    private static final int FINISH_LAUNCHER = 0;
    private Handler UiHandler = new MyHandler();

    private AdView mAdView;


    @Inject
    SPDao spDao;

    /**
     * 接受消息，处理消息 ，此Handler会与当前主线程一块运行，，只为测试只为测试只为测试只为测试
     */
    class MyHandler extends Handler {

        public MyHandler() {

        }

        // 子类必须重写此方法，接受数据
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FINISH_LAUNCHER:
                    //没有登陆过就去指导页面（Guide Page）
                    Intent i1 = new Intent();
                    i1.setClass(NewsLauncherActivity.this, NewsPackageActivity.class);
//                    i1.setClass(NewsLauncherActivity.this, NewsActivity.class);
                    startActivity(i1);
                    NewsLauncherActivity.this.finish();

                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_layout);

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713

        MobileAds.initialize(this, "ca-app-pub-8621230724267558~7770389405");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                int a=1;
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                int a=ERROR_CODE_INTERNAL_ERROR;
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                int a=1;
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                int a=1;
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                int a=1;

            }
        });


        UiHandler.sendEmptyMessageDelayed(FINISH_LAUNCHER, 3000);    //测试内存泄漏,只为测试.
    }


}
