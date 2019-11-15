package com.anylife.module_main.thirdpartyAPI.model;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.anylife.module_main.thirdpartyAPI.http3service.BlogApiService;
import com.anylife.module_main.thirdpartyAPI.http3service.Http3Observer;
import com.anylife.module_main.thirdpartyAPI.http3service.Http3Retrofit;
import com.zlb.httplib.scheduler.SwitchSchedulers;

import java.util.List;

/**
 * Module
 */
public class BlogRepository {

    public BlogRepository(Application application) {

    }

    //通常LiveData是需要配合ViewModel使用的。ViewModel负责在系统配置更改时保存和恢复LiveData，（被销毁并不会恢复）
    //而 LiveData 则负责在生命周期状态发生改变的时候，对数据的变化进行监听。

    //MutableLiveData 是有生命周期感知能力的
    private MutableLiveData<List<Blog>> mutableLiveData = new MutableLiveData<>();

    public MutableLiveData<List<Blog>> getMutableLiveData() {
        Http3Retrofit.getRetrofit().create(BlogApiService.class).getPopularBlog()
                .compose(SwitchSchedulers.applySchedulers())
                //BaseObserver 参数问题优化，如果不传参数context 的话，依赖context 的功能就要改
                .subscribe(new Http3Observer<List<Blog>>(null) {
                    @Override
                    public void onSuccess(List<Blog> blogList) {
                        mutableLiveData.setValue(blogList);
                    }
                });
        return mutableLiveData;
    }

}
