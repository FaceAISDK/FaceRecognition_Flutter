package com.anylife.module_main.thirdpartyAPI.http3service;

import com.anylife.module_main.thirdpartyAPI.model.Blog;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 假定这是个新系统的对应的API Service
 *
 */
public interface BlogApiService {

    /**
     *
     * @return
     */
    @GET("feed.json")
    Observable<Http3Response<List<Blog>>> getPopularBlog();


}
