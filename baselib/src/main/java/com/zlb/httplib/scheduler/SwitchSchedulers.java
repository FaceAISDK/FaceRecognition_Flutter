package com.zlb.httplib.scheduler;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 *
 * Created by zenglb on 2017/4/20.
 */
public class SwitchSchedulers {

    /**
     * 在IO 线程请求，Main 处理结果
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> observable) {
//                Log.e("Thread-io",Thread.currentThread().getName());
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }



    /**
     * 在IO 线程请求，IO 处理结果
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> applyScheduler2() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io());
            }
        };
    }




}
