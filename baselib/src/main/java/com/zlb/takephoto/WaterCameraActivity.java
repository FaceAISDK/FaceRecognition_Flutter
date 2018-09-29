package com.zlb.takephoto;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.liaoinstan.springview.utils.DensityUtil;
import com.zlb.Sp.SPDao;
import com.zlb.base.BaseActivity;
import com.zlb.httplib.BuildConfig;
import com.zlb.httplib.R;
import com.zlb.utils.BitMapUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static com.zlb.config.FileCacheConfig.CACHE_IMAGE;

/**
 * 水印相机，后期也可以支持不要水印
 * <p>
 * 把拍照这个动作和处理封装起来，不用在每个需要拍照的地方都写那么多重复的类似的代码；人生皆苦
 * <p>
 * <p>
 * https://www.jianshu.com/p/1e69d25d97cc (组件化的FileProvider ID 动态化)
 */
@Route(path = "/camera/watermark")
public class WaterCameraActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_TAKE_WATER_PHOTO = 801;  //FOR ACT 8xxxx
    String mCurrentPhotoPath;
    ImageView previewImage;

    Button cancel;
    Button confirm;
    ImageView qrCodeIv;
    TextView name;
    TextView time;
    TextView location;
    FrameLayout waterImageLayout;
    RelativeLayout rootView;

    private double latitude = Double.MIN_VALUE;
    private double longitude = Double.MIN_VALUE;

    private CipherText cipherText = new CipherText();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkCameraPermissions();  //先Location 然后CAMERA
    }


    @Override
    protected int getLayoutId() {
        return R.layout.act_water_camera;
    }

    @Override
    protected void initViews() {
        setToolBarVisible(View.GONE);  //这里是不需要Toolbar 的


//        previewImage=$(R.id.preview_image);  //组件化 不能使用ButterKnife，能自定生成代码也好啊

        previewImage = findViewById(R.id.preview_image);
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> cancel());

        confirm = findViewById(R.id.confirm);

        confirm.setOnClickListener(v ->
                saveBmpAndReturn()
        );


        qrCodeIv = findViewById(R.id.qrCodeIv);
        name = findViewById(R.id.name);
        time = findViewById(R.id.time);

        location = findViewById(R.id.location);
        waterImageLayout = findViewById(R.id.water_image_layout);
        rootView = findViewById(R.id.rootView);
    }


    /**
     * 通知android相册更新照片
     *
     * @param ctx         上下文
     * @param imgFileName 图片名称
     */
    private void scanPhoto(Context ctx, String imgFileName) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imgFileName);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        ctx.sendBroadcast(mediaScanIntent);
    }


    /**
     * convertViewToBitmap
     *
     * @param view
     * @return
     */
    public Bitmap convertViewToBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap tBitmap = view.getDrawingCache();
        // 拷贝图片，否则在setDrawingCacheEnabled(false)以后该图片会被释放掉
        tBitmap = Bitmap.createBitmap(tBitmap);
        view.setDrawingCacheEnabled(false);
        return tBitmap;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_TAKE_WATER_PHOTO:
                if (resultCode != RESULT_OK) {
                    Intent finish = new Intent();
                    setResult(RESULT_CANCELED, finish);
                    finish();
                    return;
                }

                //需要停留半秒等待照片保存成功吗？
                Glide.with(this).load(mCurrentPhotoPath).into(previewImage);
                generateQrCode();

                break;
            default:

                break;
        }
    }


    /**
     * 生成二维码相关信息
     */
    private void generateQrCode() {
        cipherText.setOriginal_time(System.currentTimeMillis() / 1000);
        cipherText.setOriginal_time_reliability(0); //没有校准过时间，不知道本地的是否可信啊

        //下面是员工的相关信息
        name.setVisibility(View.VISIBLE);
        name.setText("unknown name");

        cipherText.setName(name.getText().toString());
        cipherText.setGeo(new CipherText.GeoBean(latitude, longitude, "中华人民共和国万岁"));

        WaterData waterData = new WaterData("version", "75438927534275489237548723985742");

        location.setText("定位获取失败");

        qrCodeIv.setImageBitmap(BitMapUtil.Create2DCode(new Gson().toJson(waterData), DensityUtil.dp2px(100)));
    }


    /**
     * 产生的素材都将统一放在Ledbang 文件下，后续需要清楚才好管理
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();


        // 产生的素材都将统一放在Le-bang 文件下，后续需要清楚才好管理
        createDirs(CACHE_IMAGE);
        File image = new File(CACHE_IMAGE + timeStamp + "_watermark.jpg");
//        File image = new File(Environment.getExternalStorageDirectory().getPath() + "/PIC_" + timeStamp + ".jpg");
        mCurrentPhotoPath = image.getAbsolutePath();

        Log.e("compress", "初始创建路径：" + mCurrentPhotoPath);
        return image;
    }


    /**
     * 创建图片目录
     *
     * @param dirPath
     * @return
     */
    private static boolean createDirs(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdirs();
        }
        return true;
    }


    /**
     * 打开系统相机
     * 竟然真的有用户刷机后没有了系统照相机！！！！ smz
     */
    public void openCamera() {
        File file = null;
        try {
            file = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file == null) {
            Log.e("compress", "创建文件失败");
            return;
        }

        Uri imageUri;


        /**
         * 总的来说就一句话：在Android Studio中build.gradle构建工程后，
         * manifest文件中的packagename只用作R文件的引用包名，
         * 而applicationId变成了这个程序真正的包名。
         *
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

//            imageUri = FileProvider.getUriForFile(this, "com.anylife.jsbridge.fileprovider", file);
            imageUri = FileProvider.getUriForFile(this.getApplicationContext(), getApplication().getPackageName() + ".fileprovider", file);
        } else {
            imageUri = Uri.fromFile(file);
        }

        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);          //设置Action为拍照

        try {// 尽可能调用系统相机
            String cameraPackageName = getCameraPhoneAppInfos(this);
            if (cameraPackageName == null) {
                cameraPackageName = "com.android.camera";
            }
            final Intent intentCamera = getPackageManager().getLaunchIntentForPackage(cameraPackageName);
            if (intentCamera != null) {
                intent.setPackage(cameraPackageName);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);         //将拍取的照片保存到指定URI
        startActivityForResult(intent, REQUEST_TAKE_WATER_PHOTO);
    }


    /**
     * 判断支持MediaStore.ACTION_IMAGE_CAPTURE的intent且为系统应用，应该就是系统相机了吧
     *
     * @param context
     * @return
     */
    private String getCameraPhoneAppInfos(Activity context) {
        try {
            String strCamera = "";
            Intent infoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            List<ResolveInfo> infos = context.getPackageManager().queryIntentActivities(infoIntent, 0);

            if (infos != null && infos.size() > 0) {
                for (ResolveInfo info : infos) {
                    int flags = info.activityInfo.applicationInfo.flags;
                    if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) { //系统相机
                        strCamera = info.activityInfo.packageName;
                        break;
                    }
                }
            }
            if (strCamera != null) {
                return strCamera;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("picPathCache", mCurrentPhotoPath);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mCurrentPhotoPath = savedInstanceState.getString("picPathCache");
        super.onRestoreInstanceState(savedInstanceState);
    }


    /**
     * 取消保存照片
     */
    private void cancel() {
        Intent data = new Intent();
        setResult(RESULT_CANCELED, data);
//        FileUtils.delete(mCurrentPhotoPath);  //删除拍的照片
        finish();
    }


    public final static String PHOTO_PATH_KEY = "PHOTO_PATH_KEY";

    /**
     * 保存水印图片到SD 并返回
     */
    private void saveBmpAndReturn() {
        BitMapUtil.saveBmpToSD(convertViewToBitmap(waterImageLayout), mCurrentPhotoPath);
        scanPhoto(this, mCurrentPhotoPath);

        Intent confirm = new Intent();
        confirm.putExtra(PHOTO_PATH_KEY, mCurrentPhotoPath);
        setResult(RESULT_OK, confirm);
        finish();
    }


    /**
     * 用户授权了定位的权限,
     */
    private static final int RC_PERMISSION = 1001;       //请求定位权限的Code

    private void checkCameraPermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            openCamera();
            rootView.setVisibility(View.VISIBLE);

        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.water_camera_permission_tips),
                    RC_PERMISSION, perms);
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
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        switch (requestCode) {
            case RC_PERMISSION:
                if (perms.contains(Manifest.permission.CAMERA) && perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    openCamera();
                    rootView.setVisibility(View.VISIBLE);
                }
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
            case RC_PERMISSION:
                if (perms.contains(Manifest.permission.CAMERA) || perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(WaterCameraActivity.this);
                    builder.setTitle(R.string.water_camera_permission_title_tips);
                    builder.setMessage(R.string.water_camera_permission_deny_tips);
                    builder.setCancelable(false);

                    builder.setPositiveButton(R.string.settings_settings, (dialog, which) -> {
                        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:" + WaterCameraActivity.this.getPackageName()));
                        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        WaterCameraActivity.this.startActivity(myAppSettings);

                        WaterCameraActivity.this.finish();//最后的选择
                    });
                    AlertDialog dlg = builder.create();
                    dlg.show();
                    dlg.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                }
                break;

        }
    }


    /**
     * 二维码数据，还是用JSON吧！拼接使用的使用多麻烦，加字段的时候多麻烦！
     */
    public class WaterData {
        String version;
        String text;

        public WaterData(String version, String text) {
            this.version = version;
            this.text = text;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

}
