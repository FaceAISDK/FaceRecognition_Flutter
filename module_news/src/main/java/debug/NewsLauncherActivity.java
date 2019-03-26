package debug;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.zenglb.framework.goodlife.R;
import com.zenglb.framework.news.handylife.NewsPackageActivity;
import com.zlb.Sp.SPDao;
import javax.inject.Inject;

import es.dmoral.toasty.Toasty;

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

                    i1.putExtra("name", "avcd");
                    i1.putExtra("syscode", "023e2e12ed");
                    //如果需要认证信息这里还有准备登陆的部分呢

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

        UiHandler.sendEmptyMessageDelayed(FINISH_LAUNCHER, 3500);  //测试内存泄漏,只为测试.
    }
}
