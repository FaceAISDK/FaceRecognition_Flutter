package com.zenglb.framework.modulea.mvp.handylife;


import com.zenglb.framework.modulea.http.result.AnyLifeResult;

import java.util.List;
import javax.inject.Inject;

/**
 * API  失效了，改天再找一个
 *
 * The Presenter of the HandyLife
 * <p>
 * Created by zlb on 2018/3/23.
 */
public class AnyLifePresenter implements AnyLifeContract.HandyLifePresenter {

    AnyLifeRepository mHandyLifeRepository;       //Model
    AnyLifeContract.HandyLifeView mHandyLifeView; //The V of the MVP

    @Inject
    public AnyLifePresenter(AnyLifeRepository mHandyLifeRepository) {
        this.mHandyLifeRepository = mHandyLifeRepository;
    }

    /**
     * 获取HandyLife data
     *
     * @param type 数据类型，{city guide,shop,eat}
     * @param page page index
     */
    @Override
    public void getHandyLifeData(String type, int page) {

        mHandyLifeRepository.getHandyLifeData(type, page, new IAnyLifeDataSource.LoadHandyLifeDataCallback() {
            @Override
            public void onHandyLifeDataSuccess(List<AnyLifeResult> handyLifeResultBeans) {
                if (null != mHandyLifeView) {
                    mHandyLifeView.showHandyLifeData(handyLifeResultBeans);
                }
            }

            @Override
            public void onHandyLifeDataFailed(int code,String message) {
                if (null != mHandyLifeView) {
                    mHandyLifeView.getHandyLifeDataFailed(code, message);
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
    public void takeView(AnyLifeContract.HandyLifeView view) {
        mHandyLifeView = view;
    }


    /**
     * dropView
     */
    @Override
    public void dropView() {
        mHandyLifeView = null;
    }


}
