package com.zenglb.framework.goodlife.handylife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.liaoinstan.springview.utils.DensityUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zenglb.framework.goodlife.GoodLifeWebActivity;
import com.zenglb.framework.goodlife.R;
import com.zenglb.framework.goodlife.http.result.ArticlesResult;
import com.zlb.base.BaseStatusFragment;
import com.zlb.base.BaseWebViewActivity;
import com.zlb.commontips.ErrorCallback;
import com.zlb.dagger.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * fragment contain the content lists with 2 different types of items.
 * 代码仅供阅读，不可用于任何形式的商业用途
 *
 * PLK-AL10C00B20
 *
 * Created by zlb on 2018/3/23.
 */
@ActivityScope
public class GoodLifeFragment extends BaseStatusFragment implements GoodLifeContract.GoodLifeView {
    private static final int perPageSize = 20;
    private static final String ARG_DATA_TYPE = "data_type";      //data type {cityguide,shop,eat}

    private int page = 1;   //假设我们的Page 都是从1开始
    private RecyclerView mRecyclerView = null;
    private GoodLifeAdapter handyLifeAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<ArticlesResult.ArticlesBean> handyLifeResultBeans = new ArrayList<>();

    @Inject
    GoodLifePresenter mPresenter;  //dagger

    /**
     * 显示数据
     *
     * @param tabsResultBeansTemp
     */
    @Override
    public void showHandyLifeData(ArticlesResult tabsResultBeansTemp) {
        page++; //next page for simple
        mBaseLoadService.showSuccess();          //ui status
        handyLifeResultBeans.addAll(tabsResultBeansTemp.getArticles());
        handyLifeAdapter.notifyDataSetChanged(); // update ui

        handyLifeAdapter.setEnableLoadMore(true); //can load more
        mSwipeRefreshLayout.setRefreshing(false); //as u see

//        if (tabsResultBeansTemp.size() > perPageSize - 1) {
//            handyLifeAdapter.loadMoreComplete();
//        } else {
//            handyLifeAdapter.loadMoreEnd();  //no more data
//        }

    }


    /**
     * get Http Data failed.
     *
     * @param code
     * @param message
     */
    @Override
    public void getHandyLifeDataFailed(int code, String message) {
        if (page == 1) {
            handyLifeAdapter.notifyDataSetChanged();  //data clear ,then update ui
        }
        handyLifeAdapter.setEnableLoadMore(true);
        mSwipeRefreshLayout.setRefreshing(false);
        if (handyLifeResultBeans.size() == 0) {
            // should switch(code)
            mBaseLoadService.showCallback(ErrorCallback.class);  //for easy
        } else {
            handyLifeAdapter.loadMoreFail(); //load more failed
        }
    }

    @Inject
    public GoodLifeFragment() {

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static GoodLifeFragment newInstance(String dataType) {
        GoodLifeFragment fragment = new GoodLifeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATA_TYPE, dataType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int onCreateFragmentView() {
        return R.layout.fragment_good_life;
    }

    /**
     * refresh data
     */
    private void refresh() {
        page = 1;
        handyLifeResultBeans.clear();
        mSwipeRefreshLayout.setRefreshing(true);
        handyLifeAdapter.setEnableLoadMore(false);  //这里的作用是防止下拉刷新的时候还可以上拉加载
        mBaseLoadService.showSuccess();

        for(int i=0;i<33;i++){
            mPresenter.getHandyLifeData(getArguments().getString(ARG_DATA_TYPE), page);
        }

        mPresenter.getHandyLifeData(getArguments().getString(ARG_DATA_TYPE), page);

    }


    /**
     * click common ui empty error status ui will invoke onHttpReload
     *
     * @param v
     */
    @Override
    protected void onHttpReload(View v) {
        refresh();
    }

    /**
     * Views init
     *
     * @param rootView
     */
    @Override
    public void initViews(View rootView) {
        handyLifeAdapter = new GoodLifeAdapter(getContext(), handyLifeResultBeans);
        mRecyclerView =  rootView.findViewById(R.id.recyclerView);


        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration
                .Builder(getActivity())
                .colorResId(R.color.common_gray)
                .size(1)
                .margin(0, 0)
                .build());

        mRecyclerView.setAdapter(handyLifeAdapter);

        handyLifeAdapter.setOnLoadMoreListener(() ->
                        mPresenter.getHandyLifeData(getArguments().getString(ARG_DATA_TYPE), page)
                , mRecyclerView);

        // 当列表滑动到倒数第N个Item的时候(默认是1)回调onLoadMoreRequested方法
        handyLifeAdapter.setPreLoadNumber(3);
        handyLifeAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setTag("tag"+getArguments().getString(ARG_DATA_TYPE));
        mSwipeRefreshLayout.setOnRefreshListener(() -> refresh());

        handyLifeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(), GoodLifeWebActivity.class);
                intent.putExtra(BaseWebViewActivity.URL, handyLifeResultBeans.get(position).getArticle().getContent_url());

                startActivity(intent);
            }
        });

        refresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Bind view to the presenter
        mPresenter.takeView(this);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

}
