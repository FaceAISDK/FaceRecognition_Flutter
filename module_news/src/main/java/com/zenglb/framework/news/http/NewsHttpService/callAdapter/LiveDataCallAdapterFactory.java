package com.zenglb.framework.news.http.NewsHttpService.callAdapter;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.zenglb.framework.news.http.NewsHttpService.NewsHttpResponse;
import com.zenglb.framework.news.http.result.HotNewsResult;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * 直接返回LiveData
 *
 *
 **/
public class LiveDataCallAdapterFactory extends CallAdapter.Factory {

    private static final String TAG = LiveDataCallAdapterFactory.class.getSimpleName();

    /**
     * 各种泛型，https://www.jianshu.com/p/cae76008b36b
     *
     * @param returnType
     * @param annotations
     * @param retrofit
     * @return
     */
    @SuppressWarnings("ClassGetClass")
    @Nullable
    @Override
    public CallAdapter<?, ?> get(@NotNull Type returnType, @NotNull Annotation[] annotations, @NotNull Retrofit retrofit) {
        if (getRawType(returnType) != LiveData.class) {
            return null;
        }
        //获取第一个泛型类型
        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Class<?> rawType = getRawType(observableType);
        Log.d(TAG, "rawType = " + rawType.getClass().getSimpleName());

        boolean isApiResponse = true;

        //这样子会限定一种返回格式，多服务器情况需要拓展
        if (rawType != NewsHttpResponse.class) {
            //不是返回ApiResponse类型的返回值
            isApiResponse = false;
        }

//        if (observableType instanceof ParameterizedType) {
//            throw new IllegalArgumentException("resource must be parameterized");
//        }

        return new LiveDataCallAdapter<>(observableType, isApiResponse);
    }
}
