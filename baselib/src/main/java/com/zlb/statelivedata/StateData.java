package com.zlb.statelivedata;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 带有状态的数据
 * onChange 官方只是处理成功数据的回调，我们需要封装处理网络错误,服务器自定义的一些返回码等这种状况
 * @param <T>
 */
public class StateData<T> {

    @NonNull
    private DataStatus status;

    @Nullable
    private T data;

    @Nullable
    private String failureMsg;  //有需要可以考虑拆分为code+msg

    public StateData() {
        this.status = DataStatus.CREATED;
        this.data = null;
        this.failureMsg = null;
    }

    public StateData<T> loading() {
        this.status = DataStatus.LOADING;
        this.data = null;
        this.failureMsg = null;
        return this;
    }

    public StateData<T> success(@NonNull T data) {
        this.status = DataStatus.SUCCESS;
        this.data = data;
        this.failureMsg = null;
        return this;
    }

    public StateData<T> failure(@NonNull String failureMsg) {
        this.status = DataStatus.ERROR;
        this.data = null;
        this.failureMsg = failureMsg;
        return this;
    }

    public StateData<T> complete() {
        this.status = DataStatus.COMPLETE;
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

    @Nullable
    public String getFailureMsg() {
        return failureMsg;
    }

    /**
     * 基本只需要维护SUCCESS，ERROR。LOADING 也可以省去
     *
     */
    public enum DataStatus {
        CREATED,
        SUCCESS,
        ERROR,
        LOADING,
        COMPLETE
    }

}