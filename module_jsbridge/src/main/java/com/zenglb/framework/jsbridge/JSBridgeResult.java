package com.zenglb.framework.jsbridge;


/**
 * 返回给JS 的数据
 *
 * @param <T>
 */
public class JSBridgeResult<T> {
    private int code;     //0：成功    其他：各种失败（详细看文档）
    private String error; //不成功时候的提示信息
    private T result;     //泛型T来表示object（可能是数组，也可能是对象）

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }


    private JSBridgeResult(Builder<T> builder) {
        code = builder.code;
        error = builder.error;
        result = builder.result;
    }

    /**
     * 范型T 怎么的返回来
     */
    public static final class Builder<T2> {
        private int code;
        private String error;
        private T2 result;

        public Builder() {
        }

        /**
         * @param <T2>
         * @return
         */
        static public <T2> Builder<T2> start() {
            return new Builder<>();
        }

        public Builder<T2> code(int val) {
            code = val;
            return this;
        }

        public Builder<T2> error(String val) {
            error = val;
            return this;
        }

        public Builder<T2> result(T2 val) {
            result = val;
            return this;
        }

        public JSBridgeResult<T2> build() {
            return new JSBridgeResult<>(this);
        }

    }

}
