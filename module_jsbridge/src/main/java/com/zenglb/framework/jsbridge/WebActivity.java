
package com.zenglb.framework.jsbridge;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.zlb.base.BaseWebViewActivity;
import com.zlb.config.FileCacheConfig;
import com.zlb.takephoto.WaterCameraActivity;
import com.zlb.utils.BitMapUtil;
import com.zlb.utils.FileStorage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * WIP :work in Process
 * <p>
 * 验证 JSBridge
 */
@Route(path = "/web/WebActivity")
public class WebActivity extends BaseWebViewActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private final static int CAMERA_PERMISSION_CODE = 1000;    //请求CAMERA权限的Code
    private final static int REQUEST_CAPTURE_IMG = 1001;    //相册选取

    private CallNewActForResultReceiver callNewActForResultReceiver = null;

    /**
     * JS call Native 方法的时候并不是都能在那个方法马上执行获取数据
     * 比如拍照和二维码需要启动一个新的Activity 才能获取到需要的数据
     * 这个时候就发广播过来处理把，也许会有更好的方法，但是我不知道！
     */

    public class CallNewActForResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int code = intent.getIntExtra("code", -1);
            switch (code) {

                case GET_IMG_REQUEST_CODE:
//                    Intent getImgIntent = new Intent(WebActivity.this, WaterCameraActivity.class);
//                    WebActivity.this.startActivityForResult(getImgIntent, REQUEST_CAPTURE_IMG);

                    ARouter.getInstance().build("/camera/watermark").navigation(WebActivity.this , REQUEST_CAPTURE_IMG);

                    break;

                case ZXING_REQUEST_CODE:
                    Intent zxingIntent = new Intent();
                    zxingIntent.setAction(BaseWebViewActivity.SCANQR_ACTION);
                    zxingIntent.addCategory(BaseWebViewActivity.SCANQR_CATEGORY);
                    WebActivity.this.startActivityForResult(zxingIntent, BaseWebViewActivity.ZXING_REQUEST_CODE);
                    break;

                default:
                    break;
            }
        }
    }



//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case REQUEST_TAKE_WATER_IMAGE:
//                    String pathStr = data.getStringExtra(WaterCameraActivity.PHOTO_PATH_KEY);
//                    path.setText(pathStr);
//                    Glide.with(this).load(pathStr).into(photo);
//                    break;
//            }
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url = getIntent().getStringExtra(BaseWebViewActivity.URL);
        setURL(url);

        JSBridge.register(JSBridge.exposeClassName, BridgeImpl.class);

        setWebChromeClient();

        //动态注册，在当前activity的生命周期內运行
        IntentFilter filter = new IntentFilter(BridgeImpl.filterTAG);
        callNewActForResultReceiver = new CallNewActForResultReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(callNewActForResultReceiver, filter);
    }


    /**
     * 在JavaScript中，当调用window对象的prompt方法时，会触发Java中的WebChromeClient对象的onJsPrompt方法
     * jsbridge://className:port/methodName?jsonObj
     *
     * @return 结束后把Webview 所在的进程killed ,所有的手机都OK吗？没有测试哦
     */
    private void setWebChromeClient() {
        mWebView.setWebChromeClient(new WebChromeClient() {

            /**
             * 在JavaScript中，当调用window对象的prompt方法时，会触发Java中的WebChromeClient对象的onJsPrompt方法
             * jsbridge://className:port/methodName?jsonObj
             *
             * @param view            WebView
             * @param url             file:///android_asset/index.html
             * @param message         JSBridge://NativeBridgeClsName:798787206/getImage?{"msg":"这是带给移动端的msg参数"}
             * @param defaultValue    defvale(拓展，目前没有使用)
             * @param result
             * @return
             */

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                String callBackData = JSBridge.callJavaNative(view, message);

                //了
                result.confirm(callBackData);
                return true;
            }

        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAPTURE_IMG://

                    String pathStr = data.getStringExtra(WaterCameraActivity.PHOTO_PATH_KEY);
                    backImgToJS(BitMapUtil.getSimpleByBelowLine( pathStr, 800, 800));

                    break;
                case ZXING_REQUEST_CODE:
                    Map<String, String> qrCodeData = new HashMap<>();
                    qrCodeData.put("qrcode", data.getStringExtra("qrcode"));

                    JSBridgeResult jsBridgeResult = JSBridgeResult.Builder.start()
                            .code(0)
                            .result(qrCodeData).build();

                    BridgeImpl.getCallback(ZXING_REQUEST_CODE).applyDataToJS(jsBridgeResult);

                    break;
            }
        }
    }


    /**
     * 把图片等信息回传给JS
     */

    private void backImgToJS(Bitmap bitmap) {
        if (null == bitmap) {
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        //如果图片大于100k,压缩到50%质量
        if (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        }


        Map<String, String> data = new HashMap<>();
        data.put("imgData", "data:image/jpg;base64," + Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));

        JSBridgeResult jsBridgeResult = JSBridgeResult.Builder.start()
                .code(0)
                .result(data).build();

        BridgeImpl.getCallback(GET_IMG_REQUEST_CODE).applyDataToJS(jsBridgeResult);
    }

    /**
     * 拍照，调用系统的相机进程拍照，由于拍照的进程的要消耗大量的资源；跳用的进程会被干掉
     */

    private Uri imageUri;

    private void openCamera() {
        File file = new FileStorage(FileCacheConfig.CACHE_IMAGE).createTempFile("temp.jpg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(WebActivity.this, "com.zenglb.framework.fileprovider", file);
        } else {
            imageUri = Uri.fromFile(file);
        }

        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);          //设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);         //将拍取的照片保存到指定URI
        startActivityForResult(intent, REQUEST_CAPTURE_IMG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != callNewActForResultReceiver) {
            //本地的广播接收，只能在代码中注册
            LocalBroadcastManager.getInstance(this).unregisterReceiver(callNewActForResultReceiver);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 检查权限
     */

    private void checkCameraPermission() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            openCamera();
        } else {
            EasyPermissions.requestPermissions(this, "拍照需要摄像头权限！", CAMERA_PERMISSION_CODE, perms);
        }
    }


    /**
     * Google EasyPermission, Android 连一个检查权限的完善库都没有，国内生态太乱了
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //handle to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    /**
     * 权限被授予了
     *
     * @param requestCode
     * @param perms
     */

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        switch (requestCode) {
            case CAMERA_PERMISSION_CODE:
                openCamera();
                break;
        }
    }


    /**
     * 当用户点击了不再提醒的时候的处理方式
     *
     * @param requestCode
     * @param perms
     */

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        switch (requestCode) {
            case CAMERA_PERMISSION_CODE:
                if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(WebActivity.this);
                    builder.setTitle(R.string.camera_permission_title_tips);
                    builder.setMessage(R.string.camera_permission_content_tips);
                    builder.setCancelable(false);
                    builder.setNegativeButton("退出", (dialog, which) -> WebActivity.this.finish());

                    builder.setPositiveButton("授权", (dialog, which) -> {
                        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:" + WebActivity.this.getPackageName()));
                        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        WebActivity.this.startActivity(myAppSettings);
                    });
                    AlertDialog dlg = builder.create();
                    dlg.show();
                    dlg.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);

                } else {
                    WebActivity.this.finish();
                }
        }
    }

}

