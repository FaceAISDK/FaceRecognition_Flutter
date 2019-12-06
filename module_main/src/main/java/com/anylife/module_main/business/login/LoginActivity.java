package com.anylife.module_main.business.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.anylife.module_main.R;
import com.anylife.module_main.business.navigation.MainActivityBottomNavi;
import com.anylife.module_main.http.MainModuleApiService;
import com.zlb.Sp.SPDao;
import com.zlb.Sp.SPKey;
import com.zlb.base.BaseDaggerActivity;
import com.zlb.httplib.HttpRetrofit;
import com.zlb.httplib.DefaultObserver;
import com.zlb.httplib.scheduler.SwitchSchedulers;

import java.util.HashMap;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;

/**
 * 简单的登录页面，简单的MVP 和Dagger demo
 * <p>
 * LoginActivity 的业务逻辑其实是非常的简单地，简单又常见地业务场景来说明MVP+DAGGER 等的使用。实际也可以不MVP
 * <p>
 * Created by anylife.zlb@gmail.com on 2018/1/11.
 */
@Route(path = "/login/activity")
public class LoginActivity extends BaseDaggerActivity {
    EditText etUsername;
    EditText etPassword;
    Button loginBtn;
    CardView cardview;

    private boolean isFromLaunch = false; //从哪里跳转来登录页面的

    @Inject
    SPDao spDao;

    @Inject
    MainModuleApiService mainModuleApiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginInit();

        //1,从Launcher 页面过来 2，用户主动退出 3，超时或其他页面退出（再次登录要回到那里去）
        isFromLaunch = getIntent().getBooleanExtra("isFromLaunch", false);
        if (!isFromLaunch) {
            logoutCustomComponent();
        }

    }

    /**
     * 登录的从新初始化，把数据
     */
    private void loginInit() {
        spDao.saveData(SPKey.KEY_ACCESS_TOKEN, "");
        HttpRetrofit.setToken("");
    }


    /**
     * 集成的IM 等第三方系统需要单独的退出来,因为账号体系不能联动的
     */
    private void logoutCustomComponent() {
//        RongyunIM.logout();
//        Clear Oautoken,在web 页面的时候怎么退出来
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        setToolBarVisible(View.GONE);  //这里是不需要Toolbar 的

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);

        loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(view -> goLogin());

        cardview = findViewById(R.id.cardview);

        //FBI WARMING ,账号仅用于分享交流，他用将追究法律责任
        etUsername.setText("18826562075");
        etPassword.setText("123456");

        findViewById(R.id.AITest).setOnClickListener(v -> {
            // 1. 应用内简单的跳转(通过URL跳转在'进阶用法'中)
            ARouter.getInstance().build("/ai/RegisterAndRecognize").navigation();
        });

    }


    /**
     * Login ,普通的登录和使用Rxjava 的方式都可以
     * <p>
     * 外网暂不支持访问
     */
    public void goLogin() {
        String userName = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            Toasty.error(this.getApplicationContext(), "请完整输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("phone", "18826562075");
        hashMap.put("passwd", "12345678");

        mainModuleApiService.goLogin(hashMap)
                .compose(SwitchSchedulers.applySchedulers())
                .subscribe(new DefaultObserver<LoginResult>(getContext()) {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        loginSuccess(loginResult);
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);

                        //外网暂不支持访问 ,但是里面的新闻接口是没有限制的
                        Intent i2 = new Intent(LoginActivity.this, MainActivityBottomNavi.class);
                        startActivity(i2);
                        LoginActivity.this.finish();

                    }
                });

    }


    /**
     * 登录成功  Demo 不提供Oauth认证了；就模拟数据吧"
     *
     * @param loginResult
     */
    public void loginSuccess(LoginResult loginResult) {
        //切换DB
        spDao.saveData(SPKey.KEY_LAST_ACCOUNT, etUsername.getText().toString().trim());
//        HttpRetrofit.setToken(spDao.getData(SPKey.KEY_ACCESS_TOKEN, "", String.class));
        spDao.saveData(SPKey.KEY_ACCESS_TOKEN, "Bearer " + "34254235432543242");
//        spDao.saveData(SPKey.KEY_REFRESH_TOKEN, loginResult.getRefreshToken());

        if (isFromLaunch) {
            Intent i2 = new Intent(LoginActivity.this, MainActivityBottomNavi.class);
            startActivity(i2);
            LoginActivity.this.finish();
        } else {//是来自Launcher启动的就跳转到主页面，否则从哪里来就到那里去
//            Intent i2 = new Intent(LoginActivity.this, MainActivityBottomNavi.class);
//            startActivity(i2);
            LoginActivity.this.finish();
        }
    }


    /**
     * 登录页面不允许返回，之前返回Home
     * <p>
     * App用户在其他设备上登录，原来的设备因为unOauth 弹出到登录页面
     * 竟然按返回键还能回去，假如有缓存敏感信息不是。。。
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
