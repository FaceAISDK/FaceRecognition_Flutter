package com.anylife.module_main.thirdpartyAPI.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anylife.module_main.R;
import com.anylife.module_main.thirdpartyAPI.model.Blog;
import com.anylife.module_main.thirdpartyAPI.viewmodel.MainViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * interface
 * to handle interaction events.
 * Use the {@link BlogListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlogListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private MainViewModel mainViewModel;

    protected void initViews(View view) {
        swipeRefresh = view.findViewById(R.id.swiperefresh);
        mRecyclerView = view.findViewById(R.id.blogRecyclerView);

        //ViewModel 就相当于MVP 的P了；好像没法和Dagger 结合起来使用的啊

        //当Activity重建的时候，虽然 onCreate() 方法会重新走一遍，但是这个mainViewModel实例，
        //仍然是第一次创建的那个实例，在ViewModelProviders.of(this).get(***.class)中的get方法中进行了缓存
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        //ViewModel 最终消亡是在 Activity 被销毁的时候，会执行它的onCleared()进行数据的清理。

        //去获取博客数据
        getPopularBlog();

        // 数据刷新获取数据
        swipeRefresh.setOnRefreshListener(this::getPopularBlog);

    }


    /**
     * 去获取数据
     */
    public void getPopularBlog() {
        swipeRefresh.setRefreshing(true);

        mainViewModel.getAllBlog().observe(this, new Observer<List<Blog>>() {
            @Override
            public void onChanged(@Nullable List<Blog> blogList) {
                swipeRefresh.setRefreshing(false);
                prepareRecyclerView(blogList);
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        initViews(view);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


}
