package com.zenglb.framework.modulea.demo.quick_input_things;

import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.zlb.base.BaseActivity;
import com.zenglb.framework.modulea.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

/**
 * 处理短时间内的多次重复事件，可以使用
 * 1.Rxjava  debounce
 * 2.Rxbing  这个侵入比较大
 * <p>
 * 有没有更多的优化？
 * 比如能够取消前面的查询过程
 * <p>
 * 遇到这一类的事情怎么处理，比如定位的快速回调，传感器三重定位的的快速回调，都会在短时间产生大量的数据
 * <p>
 * http://www.jianshu.com/p/055002aaf1ca
 * <p>
 * <p>
 * https://blog.csdn.net/hzl9966/article/details/51280493  使用Rxjava 代替EventBus
 * <p>
 * Created by anylife.zlb@gmail.com  on 2017/11/8.
 */
public class QuickInputThingsActivity extends BaseActivity {
    private EditText mEtSearch;
    private TextView mTvSearch;

    PublishSubject<Boolean> publishSubject = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolBarTitle("功能防抖");

        publishSubject = PublishSubject.create();
        publishSubject.debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) {
                        Log.e("PublishSubject", "  PublishSubject: " + aBoolean);
                        playSoundAndVibrator(aBoolean);
                    }
                });

//        subject.onNext(false);
//        subject.onNext(false);
//        subject.onNext(true);

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_quick_input_things;
    }


    /**
     * 推送来了以后播放自定义的声音和震动
     * <p>
     * 因为Google Service 进不来中国，推送进程在后台很容易被杀了，打开App 的时候就是一堆的推送接踵而至，声音也是一片
     * 我们需要的是连续的一堆的推送来了的时候就只播放一次声音。。。
     *
     * @param isMsg 消息传入true！任务的话传入false
     */
    public void playSoundAndVibrator(boolean isMsg) {
        Uri soundUri;
        if (isMsg) {
            soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.msg);
        } else {
            soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.task);
        }
        if (soundUri != null) {
            final Ringtone ringtone = RingtoneManager.getRingtone(this, soundUri);
            if (ringtone != null) {
                ringtone.setStreamType(AudioManager.STREAM_RING);  //... ...
                ringtone.play();
            } else {
                Log.e("PushHandler", "playSounds: failed to load ringtone from uri: " + soundUri);
            }
        } else {
            Log.e("PushHandler", "playSounds: could not parse Uri: " + soundUri.toString());
        }

        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
    }


    @Override
    protected void initViews() {

        findViewById(R.id.click).setOnClickListener(v -> {
            publishSubject.onNext(true);
        });


        mEtSearch = (EditText) findViewById(R.id.edit_query);
        mTvSearch = (TextView) findViewById(R.id.edit_keywords);

        //RxBind click事件
        RxView.clicks(findViewById(R.id.edit_keywords))
                .throttleFirst(1, TimeUnit.SECONDS) //防止手快的点击，只是第一次有效
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Toast.makeText(getApplicationContext(), "this is onClick", Toast.LENGTH_SHORT).show();
                    }
                });


        //2.Rxbind 防止大数据量的处理
        RxTextView.textChanges(findViewById(R.id.edit_query))
                .debounce(600, TimeUnit.MILLISECONDS)  //防止手抖快速输入，600 毫秒无输入后再处理
                .map(new Function<CharSequence, String>() {
                    @Override
                    public String apply(CharSequence charSequence) throws Exception {

                        return new StringBuilder(charSequence).toString();
                    }
                })
                .subscribe(charSequence -> {
                    Log.e("hahah", "准备去搜索，关键字： " + charSequence);
                    mTvSearch.setText(charSequence);
                });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}