package debug;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zenglb.framework.news.DynamicProxy.Sell;
import com.zenglb.framework.news.DynamicProxy.Vendor;
import com.zenglb.framework.news.R;
import com.zenglb.framework.news.handylife.NewsPackageActivity;
import com.zlb.Sp.SPDao;
import com.zlb.utils.antigpsfake.AntiGPSFake;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.inject.Inject;



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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyAppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_layout);


        AntiGPSFake.areThereMockPermissionApps(this);
        AntiGPSFake.isMockSettingsON(this);

        Log.e("AntiGPSFake",AntiGPSFake.isMockSettingsON(this)+"  "+AntiGPSFake.areThereMockPermissionApps(this));

        //
        UiHandler.sendEmptyMessageDelayed(FINISH_LAUNCHER, 2000);    //测试内存泄漏,只为测试.






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
