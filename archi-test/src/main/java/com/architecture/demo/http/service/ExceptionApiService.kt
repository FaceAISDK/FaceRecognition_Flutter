package com.architecture.demo.http.service

import com.architecture.demo.http.myinterface.ExceptionApi
import com.architecture.httplib.base.BaseHttpApiService
import okhttp3.Interceptor


/**
 * Rename XXX  Service
 *
 */
object ExceptionApiService : BaseHttpApiService("https://httpbin.org/") {

    override fun getInterceptor(): Interceptor? {
        return null
    }

    /**
     * 再次封装一次，减少调用的代码量
     *
     */
    fun getService(): ExceptionApi {
        return getService(ExceptionApi::class.java)
    }




}