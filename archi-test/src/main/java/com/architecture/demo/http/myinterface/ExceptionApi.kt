package com.architecture.demo.http.myinterface

import com.architecture.httplib.core.HttpResult
import com.architecture.httplib.beans.HttpEmptyResponse
import retrofit2.http.GET

/**
 * 验证测试异常的情况，保留验证
 *
 */
interface ExceptionApi {

    @GET("status/404")
    suspend fun status404(
    ): HttpResult<HttpEmptyResponse>


    @GET("status/501")
    suspend fun status501(
    ): HttpResult<HttpEmptyResponse>


}