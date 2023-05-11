package com.architecture.demo.http.service

import com.architecture.demo.http.myinterface.FakerApi
import com.architecture.httplib.core.MoshiResultTypeAdapterFactory
import com.architecture.httplib.base.BaseHttpApiService
import okhttp3.Interceptor

/**
 *
 *
 */
object FakerApiService : BaseHttpApiService("https://fakerapi.it/api/v1/") {

    override fun getInterceptor(): Interceptor {
        return Interceptor { chain ->
            val builder = chain.request().newBuilder()
            builder.addHeader("Source", "source")
            chain.proceed(builder.build())
        }
    }


    /**
     * 再次封装一次，减少调用的代码量
     *
     */
    fun getService():FakerApi{
        return getService(FakerApi::class.java)
    }



    /**
     * 把服务器返回的JSON 通用的大体结构字段定义出来，方便解析
     *
     *     "status": "OK",
    "code": 200,
    "total": 15,
    "data": [
     *
     */
    override fun getHttpWrapperHandler(): MoshiResultTypeAdapterFactory.HttpWrapper{

        return object : MoshiResultTypeAdapterFactory.HttpWrapper {
            override fun getStatusCodeKey(): String {
                return "code"
            }

            override fun getErrorMsgKey(): String {
                return "status"
            }

            override fun getDataKey(): String {
                return "data"
            }

            // 这个Host 服务器对应200 表示成功
            override fun isRequestSuccess(statusCode: Int): Boolean {
                return statusCode == 2001
            }
        }
    }

}

