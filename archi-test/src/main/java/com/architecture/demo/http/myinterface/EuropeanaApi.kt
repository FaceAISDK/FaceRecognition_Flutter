package com.architecture.demo.http.myinterface

import com.architecture.demo.http.beans.EuropeanaBean
import com.architecture.httplib.core.HttpResult
import retrofit2.http.GET

/**
 * 验证测试异常的情况
 *
 *
 */
interface EuropeanaApi {

    //测试直接写死参数
    @GET("search.json?wskey=gogesqui&query=Paris&reusability=open&media=true")
    suspend fun getEuropData(): HttpResult<EuropeanaBean>




}