package com.zenglb.framework.news.news;

import com.zenglb.framework.news.http.NewsHttpService.NewsApiService;
import com.zenglb.framework.news.http.NewsHttpService.NewsHttpObserver;
import com.zenglb.framework.news.http.result.HotNewsResult;
import com.zlb.statelivedata.StateLiveData;
import com.zlb.httplib.scheduler.SwitchSchedulers;
import com.zlb.persistence.dbmaster.DaoSession;

import javax.inject.Inject;

/**
 * 三星白兰地 五月黄梅天
 * Module
 */
public class NewsRepository {
    @Inject
    DaoSession daoSession;

    @Inject
    NewsApiService newsApiService;

    @Inject
    public NewsRepository() {

    }

    //MutableLiveData 是有生命周期感知能力的
    private StateLiveData<HotNewsResult> stateLiveData = new StateLiveData<>();


    /**
     * 数据改变会自动调用Change,多爽啊
     *
     * @return
     */
    public StateLiveData<HotNewsResult> getStateLiveData() {
        //其实放在UI 的请求开始也是可以的，没有必要一定放在这里
        stateLiveData.postLoading();

        //1.异步加载网络请求数据
//        BlogHttpRetrofit.getRetrofit().create(BlogApiService.class) //使用Dagger可以省很多代码
        newsApiService.getNews()
                .compose(SwitchSchedulers.applyScheduler2())
                .subscribe(new NewsHttpObserver<HotNewsResult>() {
                    @Override
                    public void onSuccess(HotNewsResult hotNewsResult) {
                        //更新为网络数据
                        stateLiveData.postSuccess(hotNewsResult);
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                        //把错误信息告知给UI操作层面
                        stateLiveData.postFailure(message + code);
                    }
                });

        //2.先返回数据库的，这里就不显示数据库了，见BlogRepository
        return stateLiveData;
    }

}
