package com.anylife.module_main.business.navigation.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.anylife.module_main.R;
import com.anylife.module_main.http.MainModuleApiService;
import com.anylife.module_main.http.result.MeProfile;
import com.anylife.module_main.blog.ui.BlogListActivity;
import com.zlb.Sp.SPDao;
import com.zlb.base.BaseStatusFragment;
import com.zlb.dagger.scope.ActivityScope;
import com.zlb.httplib.DefaultObserver;
import com.zlb.httplib.dialog.HttpUiTips;
import com.zlb.httplib.scheduler.SwitchSchedulers;
import com.zlb.persistence.dbmaster.DaoSession;
import com.zlb.utils.ntp.SyncNtpTimeUtils;

import javax.inject.Inject;

/**
 * 我的Fragment
 */
@ActivityScope
public class MeFragment extends BaseStatusFragment implements View.OnClickListener {

    private Button httpReq,jetPack;
    private TextView meProfile;
    private Button logout;

    @Inject
    SPDao spDao;

    @Inject
    DaoSession daoSession;

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
     */
    private void getMeProfile() {
        mainModuleApiService.getMeProfile()
                .compose(SwitchSchedulers.applySchedulers())
                .subscribe(new DefaultObserver<MeProfile>(getActivity()) {
                    @Override
                    public void onSuccess(MeProfile meProfileResult) {
                        meProfile.setText(meProfileResult.toString());
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                    }
                });

    }


    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }


    @Inject
    SyncNtpTimeUtils ntpUtils;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.httpReq) {
            callPhone("10086");

//            HttpUiTips.showDialog(getActivity(), "");
//            ntpUtils.syncNTPTime();
        } else if (view.getId() == R.id.logout) {
            ARouter.getInstance().build("/login/activity").navigation();
//            getActivity().finish();
        }else if (view.getId() == R.id.jetpack) {
//            ARouter.getInstance().build("/jetpack/activity").navigation();

            startActivity(new Intent(getContext(), BlogListActivity.class));
        }
    }


    public void initView(View rootView) {
        httpReq = (Button) rootView.findViewById(R.id.httpReq);
        jetPack = (Button) rootView.findViewById(R.id.jetpack);
        jetPack.setOnClickListener(MeFragment.this);

        httpReq.setOnClickListener(MeFragment.this);
        meProfile = (TextView) rootView.findViewById(R.id.meProfile);
        logout = (Button) rootView.findViewById(R.id.logout);
        logout.setOnClickListener(MeFragment.this);
    }

}
