package com.anylife.module_main.blog.viewmodel;

import androidx.lifecycle.ViewModel;

import com.zlb.statelivedata.StateLiveData;
import com.zlb.persistence.entity.Blog;
import com.anylife.module_main.blog.model.BlogRepository;
import java.util.List;
import javax.inject.Inject;

/**
 * 理论情况下，ViewModel 不需要知道任何关于 Android 的东西。
 * 这提供了可测试性，防止内存泄漏和模块化的好处。
 * 一条基本规制是确保在你的 ViewModels 类中没有任何 android.* 的类导入（android.arch.* 例外）
 *
 *
 */
public class BlogViewModel extends ViewModel {

    @Inject
    BlogRepository blogRepository;

    /**
     *
     */
    @Inject
    public BlogViewModel() {

    }


    /**
     * 获取带有加载状态的数据 StateLiveData
     *
     * @return
     */
    public StateLiveData<List<Blog>> getAllBlog(){
        return blogRepository.getStateLiveData();
    }


    /**
     * 也可以有getTags 等等
     *
     * @return
     */
    public StateLiveData<List<Blog>> getTags(){
        return blogRepository.getStateLiveData();
    }

}
