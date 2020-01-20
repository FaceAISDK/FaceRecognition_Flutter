package com.zenglb.framework.news.http.NewsHttpService;

import androidx.annotation.Nullable;
import com.zlb.httplib.BaseObserver;

/**
 * 第三方系统的APi 结构很难会是一样的，有些是Code=0表示成功，而有些是code=200 表示成功
 * 有些是data 里面有我们实际想要的数据，而有些是再result里面。所以抽象BaseObserver后
 * 重写Http3Observer
 * <p>
 * <p>
 * <p>
 * Created by zenglb on 2019/11/11.
 */
public abstract class NewsHttpObserver<T> extends BaseObserver<NewsHttpResponse<T>> {

    public NewsHttpObserver() {
        super();
    }


    /**
     * 根据具体的Api 业务逻辑去重写 onSuccess 方法！Error 是选择重写，but 必须Super ！
     *
     * @param t
     */
    public abstract void onSuccess(@Nullable T t);


    @Override
    public final void onNext(NewsHttpResponse<T> response) {
        //这里根据具体的业务情况自己定义吧
        if (!response.isError()) {
            onSuccess(response.getResults());
        } else {
            onFailure(0, response.getMessage());
        }
    }


}
