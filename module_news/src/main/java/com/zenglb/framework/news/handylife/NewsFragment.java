package com.zenglb.framework.news.handylife;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zenglb.framework.news.NewsWebActivity;
import com.zenglb.framework.news.R;
import com.zenglb.framework.news.http.result.NewsBean;
import com.zlb.base.BaseStatusFragment;
import com.zlb.base.BaseWebViewActivity;
import com.zlb.dagger.scope.ActivityScope;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * 新闻周刊
 *
 * Created by zlb on 2018/3/23.
 */
@ActivityScope
public class NewsFragment extends BaseStatusFragment {
    private static final String ARG_DATA_TYPE = "data_type";
    private static final String ARG_DATA = "data_news";

    private List<NewsBean> articlesBeans = new ArrayList<>();

    @Inject
    public NewsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            articlesBeans = getArguments().getParcelableArrayList(ARG_DATA);
        }
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static NewsFragment newInstance(String dataType, ArrayList<NewsBean> homeBeans) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATA_TYPE, dataType);
        args.putParcelableArrayList(ARG_DATA, homeBeans);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int onCreateFragmentView() {
        return R.layout.fragment_news;
    }

    /**
     * click common ui empty error status ui will invoke onHttpReload
     *
     * @param v
     */
    @Override
    protected void onHttpReload(View v) {

    }

    /**
     * Views init
     *
     * @param rootView
     */
    @Override
    public void initView(View rootView) {
        NewsAdapter newsAdapter = new NewsAdapter(getContext(), articlesBeans);
        RecyclerView mRecyclerView = rootView.findViewById(R.id.newsRecyclerView);

        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration
                .Builder(getActivity())
                .colorResId(R.color.common_gray)
                .size(1)
                .margin(0, 0)
                .build());

        mRecyclerView.setAdapter(newsAdapter);

        // 当列表滑动到倒数第N个Item的时候(默认是1)回调onLoadMoreRequested方法
        newsAdapter.setPreLoadNumber(0);
        newsAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);

        newsAdapter.setOnItemClickListener((adapter, view, position) -> {

            Intent intent = new Intent(getContext(), NewsWebActivity.class);
            intent.putExtra(BaseWebViewActivity.URL, articlesBeans.get(position).getLink());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

    }


}
