package com.zenglb.framework.modulea.mvp.handylife;


import com.zenglb.framework.modulea.http.result.AnyLifeResult;

import java.util.List;

/**
 * Main entry point for accessing data.
 *
 * For simplicity, All The data is load form remote,we do not use orm db to persistent data
 *
 * Created by zlb on 2018/3/23.
 */
public interface IAnyLifeDataSource {

    void getHandyLifeData(String type, int page,LoadHandyLifeDataCallback loadHandyLifeDataCallback) ;


    /**
     *  the callback of getHandyLifeData
     */
    interface LoadHandyLifeDataCallback {
        void onHandyLifeDataSuccess(List<AnyLifeResult> handyLifeResultBeans);
        void onHandyLifeDataFailed(int code,String message);
    }

}
