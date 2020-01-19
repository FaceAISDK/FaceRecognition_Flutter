package com.zenglb.framework.news.http.NewsHttpService;

import com.zenglb.framework.news.http.result.HotNewsResult;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 假定这是个新系统的对应的API Service
 *
 */
public interface NewsApiService {

    /**
     *
     * @return
     */
    @GET("today")
    Observable<NewsHttpResponse<HotNewsResult>> getNews();


    // 下面是各种接口


}
