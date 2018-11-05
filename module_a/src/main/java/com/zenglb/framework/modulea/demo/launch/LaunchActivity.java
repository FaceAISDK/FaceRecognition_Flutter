package com.zenglb.framework.modulea.demo.launch;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.zenglb.framework.modulea.R;
import com.zlb.base.BaseMVPActivity;
import com.zenglb.framework.modulea.mvp.login.LoginActivity;
import com.zlb.Sp.SPDao;
import com.zlb.Sp.SPKey;
import com.zenglb.framework.modulea.navigation.MainActivityBottomNavi;

import java.util.List;

import javax.inject.Inject;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 启动页，并使所有的UI 的模型都需要MVP，复杂的才用
 * <p>
 * Created by anylife.zlb@gmail.com on 2017/1/11.
 */
public class LaunchActivity extends BaseMVPActivity implements EasyPermissions.PermissionCallbacks {

    private static final int FINISH_LAUNCHER = 0;
    private Handler UiHandler = new MyHandler();

    private static final String[] PERMISSION_LIST =
            {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    private static final int RC_LOCATION_CONTACTS_PERM = 1001;

    @Inject
    SPDao spDao;

    private AdView mAdView;

    private InterstitialAd mInterstitialAd;

    /**
     * 接受消息，处理消息 ，此Handler会与当前主线程一块运行，只为测试只为测试只为测试只为测试
     */
    class MyHandler extends Handler {

        public MyHandler() {

        }

        // 子类必须重写此方法，接受数据
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FINISH_LAUNCHER:
                    String accessToken = spDao.getData(SPKey.KEY_ACCESS_TOKEN, "", String.class);
                    //没有登陆过就去指导页面（Guide Page）
                    if (TextUtils.isEmpty(accessToken)) {
                        Intent i1 = new Intent();
                        i1.putExtra("isFromLaunch", true);

                        i1.setClass(LaunchActivity.this, LoginActivity.class);

                        startActivity(i1);
                        LaunchActivity.this.finish();
                    } else {
                        Intent i1 = new Intent();
                        i1.setClass(LaunchActivity.this, MainActivityBottomNavi.class);
                        startActivity(i1);
                        LaunchActivity.this.finish();
                    }
                    break;

                default:

                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (hasAllPermissions()) {
            UiHandler.sendEmptyMessageDelayed(FINISH_LAUNCHER, 2500);  //测试内存泄漏,只为测试.
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LaunchActivity.this);
        builder.setTitle(R.string.water_camera_permission_title_tips);
        builder.setMessage(R.string.water_camera_permission_deny_tips);
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.settings_settings, (dialog, which) -> {
            Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + LaunchActivity.this.getPackageName()));
            myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
            myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            LaunchActivity.this.startActivity(myAppSettings);
            LaunchActivity.this.finish();//最后的选择
        });
        AlertDialog dlg = builder.create();
        dlg.show();
        dlg.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolBarVisible(View.GONE);  //这里是不需要Base 中的Toolbar,不要的情况毕竟是少数

        //bg_splash 是很长的图   bg_splash2 是短的图！  测试适配，测试适配
        scaleImage(LaunchActivity.this, findViewById(R.id.launch_img), R.drawable.bg);

        testAD();

        requestAllPermissions();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UiHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideBottomUIMenu();
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }


    public void requestAllPermissions() {
        if (hasAllPermissions()) {
            UiHandler.sendEmptyMessageDelayed(FINISH_LAUNCHER, 2500);  //测试内存泄漏,只为测试.
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                    this,
                    "你，给我这些权限玩玩",
                    RC_LOCATION_CONTACTS_PERM,
                    PERMISSION_LIST);
        }
    }


    private boolean hasAllPermissions() {
        return EasyPermissions.hasPermissions(this, PERMISSION_LIST);
    }


    /**
     * 裁剪Bitmap 适配屏幕,屏幕的上下边或者左右边可能会被裁剪；所以不要放内容在外边的 X/6 圈内
     *
     * @param activity
     * @param view
     * @param drawableResId
     */
    public void scaleImage(final Activity activity, final ImageView view, int drawableResId) {
        // 获取屏幕的高宽
        Point outSize = new Point();
        activity.getWindow().getWindowManager().getDefaultDisplay().getSize(outSize);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        Bitmap originBitmap = BitmapFactory.decodeResource(activity.getResources(), drawableResId, options);

        int originWidth1 = options.outWidth;
        int originHeight1 = options.outHeight;       //和下面的不一样？https://blog.csdn.net/qq_31285265/article/details/48780667

        int originWidth = originBitmap.getWidth();   //不一样 ？？？？options.outWidth
        int originHeight = originBitmap.getHeight();

        //图片比例变换成图片的长（宽）等于屏幕的长（宽），而另外一边不小于屏幕的宽（长）
        if ((float) outSize.y / outSize.x > (float) originHeight / originWidth) {
            //图片的左右两边要截掉
            int cuteWidth = (int) (originWidth - (float) outSize.x * originHeight / outSize.y); //
            view.setImageBitmap(Bitmap.createBitmap(originBitmap, cuteWidth / 2, 0, originWidth - cuteWidth, originHeight));
        } else if ((float) outSize.y / outSize.x < (float) originHeight / originWidth) {
            //图片的上下两边要截掉
            int cuteHeight = (int) (originHeight - (float) outSize.y * originWidth / outSize.x); //
            view.setImageBitmap(Bitmap.createBitmap(originBitmap, 0, cuteHeight / 2, originWidth, originHeight - cuteHeight));
        } else {
            view.setImageResource(drawableResId);
        }
    }

    /**
     *
     */
    private void testAD() {
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-8621230724267558~7770389405");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8621230724267558/8554156299");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


}
