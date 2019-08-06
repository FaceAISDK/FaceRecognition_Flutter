package com.anylife.module_main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.anylife.module_main.business.login.LoginActivity;
import com.anylife.module_main.business.navigation.MainActivityBottomNavi;
import com.zlb.Sp.SPDao;
import com.zlb.Sp.SPKey;
import com.zlb.base.BaseDaggerActivity;

import java.util.List;

import javax.inject.Inject;

import pub.devrel.easypermissions.EasyPermissions;

/**
 *
 *
 * Created by anylife.zlb@gmail.com on 2017/1/11.
 */
public class ModuleMainLauncherActivity extends BaseDaggerActivity implements EasyPermissions.PermissionCallbacks {

    private static final int FINISH_LAUNCHER = 0;
    private Handler UiHandler = new MyHandler();

    private static final String[] PERMISSION_LIST =
            {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    private static final int RC_LOCATION_CONTACTS_PERM = 1001;

    @Inject
    SPDao spDao;

    /**
     * 接受消息，处理消息 ，此Handler会与当前主线程一块运行
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
                        i1.setClass(getContext(), LoginActivity.class);

                        startActivity(i1);
                        ModuleMainLauncherActivity.this.finish();
                    } else {
                        Intent i1 = new Intent();
                        i1.setClass(ModuleMainLauncherActivity.this, MainActivityBottomNavi.class);
                        startActivity(i1);
                        ModuleMainLauncherActivity.this.finish();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ModuleMainLauncherActivity.this);
        builder.setTitle(R.string.water_camera_permission_title_tips);
        builder.setMessage(R.string.water_camera_permission_deny_tips);
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.settings_settings, (dialog, which) -> {
            Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + ModuleMainLauncherActivity.this.getPackageName()));
            myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
            myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ModuleMainLauncherActivity.this.startActivity(myAppSettings);
            ModuleMainLauncherActivity.this.finish();//最后的选择
        });
        AlertDialog dlg = builder.create();
        dlg.show();
        dlg.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolBarVisible(View.GONE);  //这里是不需要Base 中的Toolbar,不要的情况毕竟是少数
        requestAllPermissions();
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_module_main_launcher;
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
            UiHandler.sendEmptyMessageDelayed(FINISH_LAUNCHER, 1500);  //测试内存泄漏,只为测试.
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


}