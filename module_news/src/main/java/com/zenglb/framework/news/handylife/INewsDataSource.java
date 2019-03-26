package com.zenglb.framework.news.handylife;

import com.zenglb.framework.news.http.result.ArticlesResult;


/**
 * Main entry point for accessing data.
 *
 * For simplicity, All The data is load form remote,we do not use orm db to persistent data
 *
 * Created by zlb on 2018/3/23.
 */
public interface INewsDataSource {

    void getHandyLifeData(String type, int page, LoadNewsDataCallback loadNewsDataCallback) ;


    /**
     *  the callback of getHandyLifeData
     */
    interface LoadNewsDataCallback {
        void onHandyLifeDataSuccess(ArticlesResult handyLifeResultBeans);
        void onHandyLifeDataFailed(int code, String message);
    }

}
