package com.zenglb.framework.news.news;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zenglb.framework.news.R;
import com.zenglb.framework.news.http.NewsHttpService.NewsApiService;
import com.zenglb.framework.news.http.NewsHttpService.NewsHttpResponse;
import com.zenglb.framework.news.http.result.HotNewsResult;
import com.zlb.base.BaseDaggerActivity;

import javax.inject.Inject;


/**
 * 仅仅是调试的时候使用，没有实际的用途
 * <p>
 * (Fragment) ARouter.getInstance().build("/news/packageFragment").navigation()
 * 被组件化到了Main
 */
public class NewsPackageActivity extends BaseDaggerActivity {

    @Inject
    NewsApiService newsApiService;

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_package;
    }

    @Override
    protected void initViews() {
        setActivityTitle("News 模块组件化调试");

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, (Fragment) ARouter.getInstance().build("/news/packageFragment").navigation())
                .commit();


        /**
         * 简单的功能，代码不区分MVP 之类的
         * 简单的处理的演示
         *
         */
        newsApiService.getNewsLiveData().observe(this, new Observer<NewsHttpResponse<HotNewsResult>>() {
            @Override
            public void onChanged(NewsHttpResponse<HotNewsResult> hotNewsResult) {
                if(hotNewsResult.isError()){
                    //失败

                }else{
                    //成功

                }
            }
        });


    }

    @Override
    public boolean isShowBackIcon() {
        return false;
    }

}
