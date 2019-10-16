package debug;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import com.zenglb.framework.news.DynamicProxy.Sell;
import com.zenglb.framework.news.DynamicProxy.Vendor;
import com.zenglb.framework.news.R;
import com.zenglb.framework.news.fileDownLoad.FileDownLoadObserver;
import com.zenglb.framework.news.handylife.NewsPackageActivity;
import com.zenglb.framework.news.http.NewsApiService;
import com.zlb.Sp.SPDao;
import com.zlb.http.HttpRetrofit;
import com.zlb.utils.antigpsfake.AntiGPSFake;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 单独的Module 也需要Launcher
 *
 */
public class NewsLauncherActivity extends AppCompatActivity {

    private static final int FINISH_LAUNCHER = 0;
    private Handler UiHandler = new MyHandler();

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
                    startActivity(i1);
                    NewsLauncherActivity.this.finish();
                    break;

                default:
                    break;
            }
        }
    }





    /**
     * 下载单文件，该方法不支持断点下载
     *
     * @param url                  文件地址
     * @param destDir              存储文件夹
     * @param fileName             存储文件名
     * @param fileDownLoadObserver 监听回调
     */
    public void downloadFile(@NonNull String url, final String destDir, final String fileName, final FileDownLoadObserver<File> fileDownLoadObserver) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl(HttpRetrofit.baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit
                .create(NewsApiService.class)
                .downLoadFile(url)
                .subscribeOn(Schedulers.io())//subscribeOn和ObserOn必须在io线程，如果在主线程会出错
                .observeOn(Schedulers.io())
                .observeOn(Schedulers.computation())//需要
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(@NonNull ResponseBody responseBody) throws Exception {
                        return fileDownLoadObserver.saveFile(responseBody, destDir, fileName);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileDownLoadObserver);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyAppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_layout);

        AntiGPSFake.areThereMockPermissionApps(this);
        AntiGPSFake.isMockSettingsON(this);

        Log.e("AntiGPSFake",AntiGPSFake.isMockSettingsON(this)+"  "+AntiGPSFake.areThereMockPermissionApps(this));

        UiHandler.sendEmptyMessageDelayed(FINISH_LAUNCHER, 12000);    //测试内存泄漏,只为测试.



        downloadFile("https://down.szhtxx.cn/downPdf/440301999999980/2019/08/15/c8630cee58664229a0b7a87eb6a07f27.pdf", Environment.getExternalStorageDirectory().getPath(),"test1111.PDF",new FileDownLoadObserver<File>() {
            @Override
            public void onDownLoadSuccess(File file) {
                File file1=file;
                Log.e("TAG1111111111",Thread.currentThread().getName()+file.toString());

            }
            @Override
            public void onDownLoadFail(Throwable throwable) {
                int a=0;
            }

            @Override
            public void onProgress(int progress,long total) {
                Log.e("TAG1111111111",Thread.currentThread().getName());
            }
        });






        //动态代理测试
        Sell sell = (Sell) Proxy.newProxyInstance(Sell.class.getClassLoader(), new Class<?>[]{Sell.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Log.e("HHHH", "Before");
                Object object = method.invoke(new Vendor(), args);
                Log.e("HHHH", "After");
                return object;
            }
        });

        sell.ad();



        //==========================

        Sell sell1=(Sell) Proxy.newProxyInstance(Sell.class.getClassLoader(), new Class<?>[]{Sell.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Log.e("HHHH", "Before");
                Object object = method.invoke(new Vendor(), args);
                Log.e("HHHH", "After");
                return object;
            }
        });

    }


}
