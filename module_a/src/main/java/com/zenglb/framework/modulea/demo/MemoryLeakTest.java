package com.zenglb.framework.modulea.demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.zenglb.framework.modulea.R;
import com.zenglb.framework.modulea.http.AModuleApiService;
import com.zlb.base.BaseMVPActivity;

import com.zlb.Sp.SPDao;
import com.zlb.httplib.BaseObserver;
import com.zlb.Sp.SPKey;
import com.zlb.httplib.dialog.ProgressDialog;
import javax.inject.Inject;


/**
 * RX 内存泄漏测试
 * https://www.jianshu.com/p/0076cb510372
 *
 *
 * 统一更正一下说法，只要会造成内存无法及时释放就是内存泄露
 *
 */
public class MemoryLeakTest extends BaseMVPActivity {

    @Inject
    SPDao spDao;

    @Inject
    AModuleApiService apiService;  //

    static ProgressDialog progressDialog;  //000000000， 这种是会永远的无法释放的内存泄漏，

    private static final int FINISH_LAUNCHER = 0;

    //可能会造成内存无法及时的释放，但是消息队列中没有消息以后就可以释放了
    private Handler UiHandler = new Handler(){
        // 子类必须重写此方法，接受数据
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FINISH_LAUNCHER:

                    // 实现 Runnable 接口
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int x=0;x<10;x++){
                                try {
                                    Thread.sleep(1000);
                                }catch (Exception e){

                                }
                                System.out.println(Thread.currentThread().getName() + ":" + x);
                            }
                        }
                    },"线程222222").start();

                    String accessToken = spDao.getData(SPKey.KEY_ACCESS_TOKEN, "", String.class);
//                    finish();

                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 接受消息，处理消息 ，此Handler会与当前主线程一块运行
     *
     */
    class MyHandler extends Handler {

        public MyHandler() {
        }

        public MyHandler(Looper L) {
            super(L);
        }

        // 子类必须重写此方法，接受数据
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FINISH_LAUNCHER:
                    String accessToken = spDao.getData(SPKey.KEY_ACCESS_TOKEN, "", String.class);
                    finish();
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        UiHandler.sendEmptyMessageDelayed(FINISH_LAUNCHER, 100);  //测试内存泄漏
        progressDialog=new ProgressDialog(this, com.zlb.httplib.R.style.CustomHttpWaitDialog,"test");

        setToolBarTitle("内存泄漏检测");
     }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_memory_leak_test;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        UiHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    /**
     * 当ACT 销毁后怎样？
     *
     */

    int time=0;
    private void initData() {

//        //222222 内存无法及时的释放，无法及时的释放
//        apiService.getJokes("expired", 1)
//                .compose(SwitchSchedulers.applySchedulers())
//                .subscribe(new BaseObserver<List<JokesResult>>(this) {
//                    @Override
//                    public void onSuccess(List<JokesResult> areuSleepResults) {
//                        Log.e("okhttp", areuSleepResults.toString());
//                        time++;
//                        if(time<6)
//                        initData();
//                    }
//
//                    @Override
//                    public void onFailure(int code, String message) {
//                        super.onFailure(code, message);
//                    }
//                });


//        //3333333 一定会导致内存泄漏，还是
//        Observable.interval(1, TimeUnit.SECONDS)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Long>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(@NonNull Long aLong) {
//                        Log.i("接收数据", String.valueOf(aLong));
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//
//                });


    }


}
