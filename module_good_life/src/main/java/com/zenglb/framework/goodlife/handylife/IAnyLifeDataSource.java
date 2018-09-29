package com.zenglb.framework.goodlife.handylife;

import com.zenglb.framework.goodlife.http.result.ArticlesResult;


/**
 * Main entry point for accessing data.
 *
 * For simplicity, All The data is load form remote,we do not use orm db to persistent data
 *
 * Created by zlb on 2018/3/23.
 */
public interface IAnyLifeDataSource {

    void getHandyLifeData(String type, int page, LoadHandyLifeDataCallback loadHandyLifeDataCallback) ;


    /**
     *  the callback of getHandyLifeData
     */
    interface LoadHandyLifeDataCallback {
        void onHandyLifeDataSuccess(ArticlesResult handyLifeResultBeans);
        void onHandyLifeDataFailed(int code, String message);
    }

}
