package com.anylife.module_main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.anylife.module_main.business.login.LoginActivity;
import com.anylife.module_main.business.navigation.MainActivityBottomNavi;
import com.anylife.module_main.workmanger.UploadWorker;
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

    private static final String[] PERMISSION_LIST =
            {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    private static final int RC_LOCATION_CONTACTS_PERM = 1001;

    @Inject
    SPDao spDao;


    @Override
    public void msgManagement(int what) {
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
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.hasPermissions(this, PERMISSION_LIST)) {
            sendMsg(FINISH_LAUNCHER, 2500);
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

        // Create charging constraint
        Constraints constraints = new Constraints.Builder()
//                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        //a WorkRequest defines how and when work should be run. Tasks may be one-off or periodic
        //测试使用WorkManger
        OneTimeWorkRequest uploadWorkRequest = new OneTimeWorkRequest
                .Builder(UploadWorker.class)
                .setConstraints(constraints) // This adds the Constraints
                .build();

        //you can now schedule it with WorkManager using the enqueue() method.
        WorkManager.getInstance(this).enqueue(uploadWorkRequest);
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
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
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
        if (EasyPermissions.hasPermissions(this, PERMISSION_LIST)) {
            sendMsg(FINISH_LAUNCHER, 2500);
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                    this,
                    "你，给我这些权限好不好！",
                    RC_LOCATION_CONTACTS_PERM,
                    PERMISSION_LIST);
        }
    }

}