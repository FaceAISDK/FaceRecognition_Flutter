package com.zlb.statelivedata;

import androidx.lifecycle.MutableLiveData;

/**
 * 含有数据加载状态的LiveData 封装处理
 * onChange 官方只是处理成功数据的回调，我们需要封装处理网络错误,服务器自定义的一些返回码等这种状况
 *
 * @param <T>
 */
public class StateLiveData<T> extends MutableLiveData<StateData<T>> {


    /**
     * 标示数据发送异常
     */
    public void postFailure(int errorCode,String errorMsg) {
        postValue(new StateData<T>().failure(errorCode,errorMsg));
    }


    /**
     * 标示成功的获取了数据
     *
     * @param data
     */
    public void postSuccess(T data) {
        postValue(new StateData<T>().success(data));
    }

}