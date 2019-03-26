package com.zenglb.framework.news.handylife;


import com.zenglb.framework.news.http.result.ArticlesResult;
import com.zlb.base.BasePresenter;
import com.zlb.base.BaseView;

/**
 * 合约，一个功能的基本只要看这个类就知道功能纲要了
 * Created by zlb on 2018/3/23.
 */
public class NewsContract {

    /**
     * 对UI 的操作的接口有哪些，一看就只明白了
     *
     */
    public interface NewsView extends BaseView<NewsPresenter> {
        void showHandyLifeData(ArticlesResult tabsResultBeans);
        void getHandyLifeDataFailed(int code, String message);
        boolean isActive();
    }

    /**
     * View 层对Presenter 的请求
     *
     */
    public interface NewsPresenter extends BasePresenter<NewsView> {
        void getHandyLifeData(String type, int page);
    }

}
