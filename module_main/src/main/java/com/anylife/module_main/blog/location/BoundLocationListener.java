package com.anylife.module_main.blog.location;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 定位
 */
public class BoundLocationListener implements LifecycleObserver {

    public interface ThingChangeListener {
        void onThingChanged(long thingId);
    }

    private ThingChangeListener thingChangeListener;
    private Disposable disposable;

    private boolean isActive;

    /**
     * 绑定定位的监听器
     */
    public BoundLocationListener(ThingChangeListener thingChangeListener) {
        this.thingChangeListener = thingChangeListener;
    }


    /**
     * 模拟定位的，其实就是数据的更改
     */
    private void changeThing() {
        if(disposable==null){
            disposable = Observable.interval(1, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    Log.e("LIFE","aaa:"+aLong);
                    if(isActive){
                        thingChangeListener.onThingChanged(aLong);
                        Log.w("LIFE","bbb:"+aLong);
                    }
                }
            });
        }
    }


    /**
     * Resume 定位
     *
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void addLocationListener() {
        isActive=true;
        changeThing();
    }


    /**
     * 暂停
     *
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void pauseLocationListener() {
        isActive=false;
    }


    /**
     * 停止定位的更新
     *
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void removeLocationListener() {
        disposable.dispose();
    }

}
