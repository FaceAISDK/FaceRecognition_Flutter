package com.anylife.module_main.blog.model;

import com.zlb.statelivedata.StateLiveData;
import com.anylife.module_main.blog.BlogHttpService.BlogApiService;
import com.anylife.module_main.blog.BlogHttpService.BlogHttpObserver;
import com.zlb.httplib.scheduler.SwitchSchedulers;
import com.zlb.persistence.dbmaster.DaoSession;
import com.zlb.persistence.entity.Blog;

import java.util.List;

import javax.inject.Inject;

/**
 * 三星白兰地 五月黄梅天
 * Module
 */
public class BlogRepository {
    @Inject
    DaoSession daoSession;

    @Inject
    BlogApiService blogApiService;

    @Inject
    public BlogRepository() {

    }


    //MutableLiveData 是有生命周期感知能力的
    private StateLiveData<List<Blog>> stateLiveData = new StateLiveData<>();

    public StateLiveData<List<Blog>> getStateLiveData() {
        //其实放在UI 的请求开始也是可以的，没有必要一定放在这里
        stateLiveData.postLoading();

        //1.异步加载网络请求数据
//        BlogHttpRetrofit.getRetrofit().create(BlogApiService.class) //使用Dagger可以省很多代码
        blogApiService.getPopularBlog()
                .compose(SwitchSchedulers.applyScheduler2())
                .subscribe(new BlogHttpObserver<List<Blog>>() {
                    @Override
                    public void onSuccess(List<Blog> blogList) {
                        //更新为网络数据
                        stateLiveData.postSuccess(blogList);

                        //缓存数据
                        daoSession.getBlogDao().deleteAll();
                        daoSession.getBlogDao().insertInTx(blogList);
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                        //把错误信息告知给UI操作层面
                        stateLiveData.postFailure(message + code);
                    }
                });

        //2.先返回数据库的，等网络的请求返回后再通知UI数据改变了
        stateLiveData.postSuccess(daoSession.getBlogDao().loadAll());

        return stateLiveData;
    }

}
