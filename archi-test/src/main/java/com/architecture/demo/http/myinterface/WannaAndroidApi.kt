package com.architecture.demo.http.myinterface

import com.architecture.demo.http.beans.BannerWithDefNPEField
import com.architecture.httplib.core.HttpResult
import retrofit2.http.GET

/**
 * wanandroid
 *
 */
interface WannaAndroidApi {

    //测试直接写死参数 suspend
    @GET("banner/json")
    suspend fun getBanner(): HttpResult<List<BannerWithDefNPEField>>



}