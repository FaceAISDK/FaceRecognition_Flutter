package com.architecture.demo.http.myinterface

import com.architecture.demo.http.beans.FakerDataBean
import com.architecture.httplib.core.HttpResult
import retrofit2.http.GET

/**
 * 可以比较简单的造出各种数据
 *
 */
interface FakerApi {

    //测试直接写死参数 suspend
    @GET("companies?_quantity=10&_locale=zh_CN")
    suspend fun getFakerData(): HttpResult<List<FakerDataBean>>



}