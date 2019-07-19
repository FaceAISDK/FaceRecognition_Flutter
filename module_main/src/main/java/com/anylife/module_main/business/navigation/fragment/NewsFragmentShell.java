package com.anylife.module_main.business.navigation.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anylife.module_main.R;

import com.zlb.dagger.scope.ActivityScope;
import com.zlb.persistence.dbmaster.DaoSession;

import javax.inject.Inject;


/**
 * 这里只是新闻资讯 Fragment 的壳Shell。主体在ModuleNews 中
 * 组件化开发的时候占位一下
 *
 * @author zenglb 2018.10.24
 */
@ActivityScope
public class NewsFragmentShell extends Fragment {


    @Inject
    public NewsFragmentShell() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_shell, container, false);
        viewsInit(rootView);

        return rootView;
    }


    /**
     * init views
     *
     * @param
     */
    private void viewsInit(View rootView) {

    }

}
