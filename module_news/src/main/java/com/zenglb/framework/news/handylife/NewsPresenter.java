package com.zenglb.framework.news.handylife;

import com.zenglb.framework.news.http.result.ArticlesResult;

import javax.inject.Inject;

/**
 * The Presenter of the HandyLife
 *
 * Created by zlb on 2018/3/23.
 */
public class NewsPresenter implements NewsContract.NewsPresenter {

    NewsRepository mNewsRepository;       //Model
    NewsContract.NewsView mNewsView;      //The V of the MVP

    @Inject
    public NewsPresenter(NewsRepository mNewsRepository) {
        this.mNewsRepository = mNewsRepository;
    }

    /**
     * 获取HandyLife data
     *
     * @param type 数据类型，{city guide,shop,eat}
     * @param page page index
     */
    @Override
    public void getHandyLifeData(String type, int page) {

        mNewsRepository.getHandyLifeData(type, page, new INewsDataSource.LoadNewsDataCallback() {
            @Override
            public void onHandyLifeDataSuccess(ArticlesResult handyLifeResultBeans) {
                if (null != mNewsView) {
                    mNewsView.showHandyLifeData(handyLifeResultBeans);
                }
            }

            @Override
            public void onHandyLifeDataFailed(int code,String message) {
                if (null != mNewsView) {
                    mNewsView.getHandyLifeDataFailed(code, message);
                }
            }
        });
    }


    /**
     * 这下面的两行能不能 Base化，你有什么建议呢？
     *
     * @param view the view associated with this presenter
     */
    @Override
    public void takeView(NewsContract.NewsView view) {
        mNewsView = view;
    }


    /**
     * dropView
     */
    @Override
    public void dropView() {
        mNewsView = null;
    }


}
