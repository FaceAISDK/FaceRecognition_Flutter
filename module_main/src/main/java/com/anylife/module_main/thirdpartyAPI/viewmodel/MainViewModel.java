package com.anylife.module_main.thirdpartyAPI.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.anylife.module_main.thirdpartyAPI.model.Blog;
import com.anylife.module_main.thirdpartyAPI.model.BlogRepository;

import java.util.List;

/**
 *
 *
 *
 * 初始化ViewModel，既然 ViewModel 与 UI Controller 无关，当然可以用作 MVP 的 Presenter 层提供 LiveData 给 View 层，因为 LiveData 绑定了 Lifecycle，所以不存* 在内存泄露的问题。除此之外，ViewModel 也可以用做 MVVM 模式的 VM 层，利用 Data Binding 直接把 ViewModel 的 LiveData 属性绑定到 xml 元素上，xml *中声明式的写法避免了很多样板代码，数据驱动 UI 的最后一步，我们只需要关注数据的变化即可，UI 的状态会自动发生变化。
 */
public class MainViewModel extends AndroidViewModel {
    //是不是和Presenter 非常的像啊

    private BlogRepository movieRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new BlogRepository(application);
    }

    /**
     *
     * @return LiveData<List<Blog>>  返回的是不可修改的LiveData而不是MutableData
     */
    public LiveData<List<Blog>> getAllBlog() {
        return movieRepository.getMutableLiveData();
    }


}
