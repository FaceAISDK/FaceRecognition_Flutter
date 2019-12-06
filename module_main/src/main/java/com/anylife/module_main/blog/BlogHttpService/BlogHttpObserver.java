package com.anylife.module_main.blog.BlogHttpService;

import androidx.annotation.Nullable;
import com.zlb.httplib.BaseObserver;

/**
 * 第三方系统的APi 结构很难会是一样的，有些是Code=0表示成功，而有些是code=200 表示成功
 * 有些是data 里面有我们实际想要的数据，而有些是再result里面。所以抽象BaseObserver后
 * 重写Http3Observer
 *
 *
 * Created by zenglb on 2019/11/11.
 */
public abstract class BlogHttpObserver<T> extends BaseObserver<BlogHttpResponse<T>> {

    /**
     * 以后都用这个，不要让UI 相关的东西传递到这里来
     */
    public BlogHttpObserver() {
        super();
    }


    /**
     * 根据具体的Api 业务逻辑去重写 onSuccess 方法！Error 是选择重写，but 必须Super ！
     *
     * @param t
     */
    public abstract void onSuccess(@Nullable T t);


    @Override
    public final void onNext(BlogHttpResponse<T> response) {
        //这里根据具体的业务情况自己定义吧,isError=false 表示成功
        if (!response.isError()) {
            onSuccess(response.getData());
        } else {
            //这里竟然没有业务错误code
            onFailure(0, response.getMessage());
        }
    }


}
