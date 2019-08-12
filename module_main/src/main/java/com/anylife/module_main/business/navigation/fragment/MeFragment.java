package com.anylife.module_main.business.navigation.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.anylife.module_main.R;
import com.anylife.module_main.http.MainModuleApiService;
import com.zlb.Sp.SPDao;
import com.zlb.base.BaseApplication;
import com.zlb.base.BaseStatusFragment;
import com.zlb.dagger.scope.ActivityScope;
import com.zlb.httplib.BaseObserver;
import com.zlb.httplib.rxUtils.SwitchSchedulers;
import com.zlb.utils.ntp.SyncNtpTimeUtils;

import javax.inject.Inject;

/**
 * 我的Fragment
 *
 */
@ActivityScope
public class MeFragment extends BaseStatusFragment implements View.OnClickListener {

    protected Button httpReq;
    protected TextView meProfile;
    protected Button logout;

    @Inject
    SPDao spDao;

    @Inject
    MainModuleApiService mainModuleApiService;


    public MeFragment() {
        // Required empty public constructor
    }

    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        return fragment;
    }


    @Override
    protected int onCreateFragmentView() {
        return R.layout.fragment_me;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getMeProfile();
    }



    /**
     * 获取我的个人信息
     *
     */
    private void getMeProfile(){
        mainModuleApiService.getMeProfile(BaseApplication.globalJniMap.get("meProfile"))
                .compose(SwitchSchedulers.applySchedulers())
                .subscribe(new BaseObserver<MeProfileResult>(getActivity()){
                    @Override
                    public void onSuccess(MeProfileResult meProfileResult) {
                        meProfile.setText(meProfileResult.toString());
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                    }
                });
    }

    @Inject
    SyncNtpTimeUtils ntpUtils;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.httpReq) {
//            for (int i=0;i<222;i++) {
//                getMeProfile();
//            }

//            ARouter.getInstance().build("/news/webActivity").navigation();

            ntpUtils.syncNTPTime();

        } else if (view.getId() == R.id.logout) {
            ARouter.getInstance().build("/login/activity").navigation();
            getActivity().finish();
        }
    }



    public void initView(View rootView) {
        httpReq = (Button) rootView.findViewById(R.id.httpReq);
        httpReq.setOnClickListener(MeFragment.this);
        meProfile = (TextView) rootView.findViewById(R.id.meProfile);
        logout = (Button) rootView.findViewById(R.id.logout);
        logout.setOnClickListener(MeFragment.this);
    }

}
