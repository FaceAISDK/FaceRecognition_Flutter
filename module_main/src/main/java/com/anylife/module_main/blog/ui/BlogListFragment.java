package com.anylife.module_main.blog.ui;

import android.content.res.Configuration;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anylife.module_main.R;
import com.zlb.base.BaseDispose;
import com.zlb.dagger.viewmodel.MyViewModelFactory;
import com.zlb.statelivedata.StateData;
import com.zlb.persistence.entity.Blog;
import com.anylife.module_main.blog.viewmodel.BlogViewModel;
import com.zlb.Sp.SPDao;
import com.zlb.base.BaseStatusFragment;
import com.zlb.dagger.scope.ActivityScope;

import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * interface
 * to handle interaction events.
 * Use the {@link BlogListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@ActivityScope
public class BlogListFragment extends BaseStatusFragment {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private BlogViewModel blogViewModel;

    @Inject
    MyViewModelFactory viewModelFactory;

    @Inject
    SPDao spDao;

    @Override
    protected int onCreateFragmentView() {
        return R.layout.fragment_video;
    }

    @Override
    public void initView(View view) {
        swipeRefresh = view.findViewById(R.id.swiperefresh);
        mRecyclerView = view.findViewById(R.id.blogRecyclerView);

        //当Activity重建的时候，虽然 onCreate() 方法会重新走一遍，但是这个mainViewModel实例，
        //仍然是第一次创建的那个实例，在ViewModelProviders.of(this).get(***.class)中的get方法中进行了缓存

//        blogViewModel = ViewModelProviders.of(this).get(BlogViewModel.class);

        //use Dagger
        blogViewModel = ViewModelProviders.of(this, viewModelFactory).get(BlogViewModel.class);


        //ViewModel 最终消亡是在 Activity 被销毁的时候，会执行它的onCleared()进行数据的清理。
        //去获取博客数据，为了简单演示，不分页
        getPopularBlog();

        // 数据刷新获取数据
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新数据
                blogViewModel.getAllBlog();
            }
        });

    }


    /**
     * 重写请求重试
     *
     * @param v
     */
    @Override
    protected void onHttpReload(View v) {
        blogViewModel.getAllBlog();
    }


    /**
     * 去获取数据
     */
    public void getPopularBlog() {
        swipeRefresh.setRefreshing(true);

        blogViewModel.getAllBlog().observe(this, new Observer<StateData<List<Blog>>>() {
            @Override
            public void onChanged(StateData<List<Blog>> stateData) {
                int a = 10;
                switch (stateData.getStatus()) {
                    case SUCCESS:
                        swipeRefresh.setRefreshing(false);
                        prepareRecyclerView(stateData.getData());
                        break;

                    case ERROR:
                        swipeRefresh.setRefreshing(false);
                        //这里可以根据msg,Code进行封装统一处理
                        BaseDispose.errorDispose(mBaseLoadService, getActivity(), stateData.getMsg(), stateData.getCode());
                        break;
                }
            }
        });
    }


    /**
     * 准备设置数据的改变
     *
     * @param blogList
     */
    private void prepareRecyclerView(List<Blog> blogList) {
        BlogAdapter mBlogAdapter = new BlogAdapter(blogList);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        }
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mBlogAdapter);
        mBlogAdapter.notifyDataSetChanged();
    }


    public BlogListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BlankFragment.
     */
    public static BlogListFragment newInstance() {
        BlogListFragment fragment = new BlogListFragment();
        return fragment;
    }


}
