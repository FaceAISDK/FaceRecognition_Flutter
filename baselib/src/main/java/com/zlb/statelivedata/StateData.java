package com.zlb.statelivedata;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 带有状态的数据
 * onChange 官方只是处理成功数据的回调，我们需要封装处理网络错误,服务器自定义的一些返回码等这种状况
 *
 * @param <T>
 */
public class StateData<T> {

    @NonNull
    private DataStatus status;

    @Nullable
    private T data;

    private int code;   //http 请求的错误的Code
    @Nullable
    private String msg;  //http 请求错误的信息提示

    public StateData() {
        this.data = null;
        this.msg = null;
    }

    public StateData<T> success(@NonNull T data) {
        this.status = DataStatus.SUCCESS;
        this.data = data;
        this.msg = null;
        return this;
    }

    /**
     *
     *
     * @param msg
     * @param code
     * @return
     */
    public StateData<T> failure(int code,@NonNull String msg) {
        this.status = DataStatus.ERROR;
        this.data = null;
        this.code = code;
        this.msg=msg;
        return this;
    }

    @NonNull
    public DataStatus getStatus() {
        return status;
    }

    @Nullable
    public T getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    @Nullable
    public String getMsg() {
        return msg;
    }

    /**
     * 基本只需要维护SUCCESS，ERROR。LOADING 也可以省去
     */
    public enum DataStatus {
        SUCCESS,
        ERROR
    }

}