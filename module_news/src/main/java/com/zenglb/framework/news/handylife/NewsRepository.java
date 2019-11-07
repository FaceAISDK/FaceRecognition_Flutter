package com.zenglb.framework.news.handylife;

import com.zenglb.framework.news.http.NewsApiService;
import com.zenglb.framework.news.http.result.ArticlesResult;
import com.zlb.httplib.BaseObserver;
import com.zlb.httplib.rxUtils.SwitchSchedulers;

import javax.inject.Inject;

/**
 * 这里就不做什么先加载本地的ORM DB 然后再加载 HTTP 了
 * <p>
 * Created by zlb on 2018/3/23.
 */
public class NewsRepository implements INewsDataSource {

    // Get the ApiService from dagger。 用dagger 来注入 ApiService
    @Inject
    NewsApiService apiService;

    @Inject
    public NewsRepository() {

    }


    /**
     * 最好加一层数据库缓存
     *
     *
     * getHandyLifeData from http server
     *
     * @param loadNewsCallback the callBack
     */
    @Override
    public void getHandyLifeData(LoadNewsDataCallback loadNewsCallback) {

        apiService.getArticles()
                .compose(SwitchSchedulers.applySchedulers())
                //BaseObserver 参数问题优化，如果不传参数context 的话，依赖context 的功能就要改
                .subscribe(new BaseObserver<ArticlesResult>(null) {
                    @Override
                    public void onSuccess(ArticlesResult lifeResultBeans) {

                        //api 服务已经不可以使用了！
                        if (null != loadNewsCallback) {
                            loadNewsCallback.onHandyLifeDataSuccess(lifeResultBeans);
                        }

                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);

                        if (null != loadNewsCallback) {
                            loadNewsCallback.onHandyLifeDataFailed(code, message);
                        }

                    }
                });

    }

}
