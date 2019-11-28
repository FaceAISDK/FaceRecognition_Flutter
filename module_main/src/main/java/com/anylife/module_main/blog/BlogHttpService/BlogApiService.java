package com.anylife.module_main.blog.BlogHttpService;

import com.zlb.persistence.entity.Blog;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 假定这是个新系统的对应的API Service
 * host（BaseUrl）不一样,返回的Api结构也不同
 *
 */
public interface BlogApiService {

    /**
     *
     * @return
     */
    @GET("feed.json")
    Observable<BlogHttpResponse<List<Blog>>> getPopularBlog();


}
