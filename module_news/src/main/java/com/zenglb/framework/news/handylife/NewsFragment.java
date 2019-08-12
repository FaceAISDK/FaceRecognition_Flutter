package com.zenglb.framework.news.handylife;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zenglb.framework.news.NewsWebActivity;
import com.zenglb.framework.news.R;
import com.zenglb.framework.news.http.result.ArticlesResult;
import com.zlb.base.BaseStatusFragment;
import com.zlb.base.BaseWebViewActivity;
import com.zlb.commontips.EmptyCallback;
import com.zlb.customview.MySwipeRefreshLayout;
import com.zlb.dagger.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import es.dmoral.toasty.Toasty;

/**
 * 这些Fragment 在常驻在主页面不会销毁，要实现懒加载，减少一下子就那么多的内存占用
 *
 *
 * 接口是没有分页的，仅仅是为了模拟分页
 * <p>
 * Created by zlb on 2018/3/23.
 */
@ActivityScope
public class NewsFragment extends BaseStatusFragment implements NewsContract.NewsView {
    //一般的没有必要实现懒加载
    protected boolean isViewInitiated;
    protected boolean isVisibleToUser;
    protected boolean isDataInitiated;

    private static final String ARG_DATA_TYPE = "data_type";
    private static final int perPageSize = 20;
    //假设我们的Page 都是从1开始
    private int page = 1;

    private RecyclerView mRecyclerView = null;
    private NewsAdapter newsAdapter;
    private MySwipeRefreshLayout mSwipeRefreshLayout;
    private List<ArticlesResult.ArticlesBean> articlesBeans = new ArrayList<>();

    @Inject
    NewsPresenter mPresenter;  //dagger

    @Inject
    public NewsFragment() {

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData(false);
    }

    /**
     * 会先于onCreate 和onCreateView 执行
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData(false);
    }

//    public abstract void fetchData();
//    public boolean prepareFetchData() {
//        return prepareFetchData(false);
//    }

    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            getNewsData();
            return true;
        }
        return false;
    }


    /**
     * 显示数据
     *
     * @param tabsResultBeansTemp
     */
    @Override
    public void showHandyLifeData(ArticlesResult tabsResultBeansTemp) {
        //全部在这里判断，上拉的时候下拉不会挂掉
        if (page == 1) {
            articlesBeans.clear();
        }
        page++;
        articlesBeans.addAll(tabsResultBeansTemp.getArticles());

        //可以加载更多数据
        newsAdapter.setEnableLoadMore(true);
        //停止刷新数据
        mSwipeRefreshLayout.setRefreshing(false);

        if (articlesBeans.size() == 0) {
            //Api 要是标准的话就自定义转换吧
            mBaseLoadService.showCallback(EmptyCallback.class);
        } else{
            mBaseLoadService.showSuccess();  // successful case -> show the data, eg RecyclerView,
            newsAdapter.notifyDataSetChanged();

            if (tabsResultBeansTemp.getArticles().size() > perPageSize - 1) {
                isDataInitiated = true;
                newsAdapter.loadMoreComplete();
            } else {
                newsAdapter.loadMoreEnd();
            }
        }

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
            articlesBeans.clear();
            newsAdapter.notifyDataSetChanged();
        }
        newsAdapter.setEnableLoadMore(true);
        mSwipeRefreshLayout.setRefreshing(false);

        if (articlesBeans.size() == 0) {
            // should switch(code)
//            Toasty.error(getContext(), message).show();
            mBaseLoadService.showCallback(EmptyCallback.class);
        } else {
            newsAdapter.loadMoreFail();
        }
    }


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static NewsFragment newInstance(String dataType) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATA_TYPE, dataType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int onCreateFragmentView() {
        return R.layout.fragment_news;
    }


    /**
     * refresh data
     */
    private void getNewsData() {
        page = 1;
        mSwipeRefreshLayout.setRefreshing(true);
        //这里的作用是防止下拉刷新的时候还可以上拉加载
        newsAdapter.setEnableLoadMore(false);
        mPresenter.getHandyLifeData(getArguments().getString(ARG_DATA_TYPE), page);
    }


    /**
     * click common ui empty error status ui will invoke onHttpReload
     *
     * @param v
     */
    @Override
    protected void onHttpReload(View v) {
        getNewsData();
    }

    /**
     * Views init
     *
     * @param rootView
     */
    @Override
    public void initView(View rootView) {
        newsAdapter = new NewsAdapter(getContext(), articlesBeans);
        mRecyclerView = rootView.findViewById(R.id.newsRecyclerView);

        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration
                .Builder(getActivity())
                .colorResId(R.color.common_gray)
                .size(1)
                .margin(0, 0)
                .build());

        mRecyclerView.setAdapter(newsAdapter);

        newsAdapter.setOnLoadMoreListener(() ->
                {
                    Log.e("Hello", "onHttpLoad: " + getArguments().getString(ARG_DATA_TYPE));
                    mPresenter.getHandyLifeData(getArguments().getString(ARG_DATA_TYPE), page);
                }
                , mRecyclerView);

        // 当列表滑动到倒数第N个Item的时候(默认是1)回调onLoadMoreRequested方法
        newsAdapter.setPreLoadNumber(0);
        newsAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setTag("tag" + getArguments().getString(ARG_DATA_TYPE));
        mSwipeRefreshLayout.setOnRefreshListener(() -> getNewsData());

        newsAdapter.setOnItemClickListener((adapter, view, position) -> {

            Intent intent = new Intent(getContext(), NewsWebActivity.class);
            intent.putExtra(BaseWebViewActivity.URL, articlesBeans.get(position).getArticle().getContent_url());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

//        refresh();
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
