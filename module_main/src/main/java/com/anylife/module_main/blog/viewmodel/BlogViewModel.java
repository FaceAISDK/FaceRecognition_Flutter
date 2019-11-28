package com.anylife.module_main.blog.viewmodel;

import androidx.lifecycle.ViewModel;

import com.zlb.statelivedata.StateLiveData;
import com.zlb.persistence.entity.Blog;
import com.anylife.module_main.blog.model.BlogRepository;
import java.util.List;
import javax.inject.Inject;

/**
 *
 * ViewModel 和Dagger 结合有点难过！
 *
 *
 * 初始化ViewModel，既然 ViewModel 与 UI Controller 无关，当然可以用作 MVP 的 Presenter 层提供 LiveData 给 View 层，因为 LiveData 绑定了 Lifecycle，所以不存* 在内存泄露的问题。除此之外，ViewModel 也可以用做 MVVM 模式的 VM 层，利用 Data Binding 直接把 ViewModel 的 LiveData 属性绑定到 xml 元素上，xml *中声明式的写法避免了很多样板代码，数据驱动 UI 的最后一步，我们只需要关注数据的变化即可，UI 的状态会自动发生变化。
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

}
