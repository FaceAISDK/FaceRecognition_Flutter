package com.architecture.demo.http.service

import com.architecture.demo.http.myinterface.WannaAndroidApi
import com.architecture.httplib.core.MoshiResultTypeAdapterFactory
import com.architecture.httplib.base.BaseHttpApiService
import okhttp3.Interceptor


/**
 *  测试域名为https://www.wanandroid.com/ 的服务
 *
 */
object WanAndroidApiService : BaseHttpApiService("https://www.wanandroid.com/") {

    override fun getInterceptor(): Interceptor {
        return Interceptor { chain ->
            val builder = chain.request().newBuilder()
            builder.addHeader("Source", "source")
            chain.proceed(builder.build())
        }
    }


    /**
     * 获取WannaAndroidApi 服务
     *
     */
    fun getService():WannaAndroidApi{
        return getService(WannaAndroidApi::class.java)
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
                return "errorCode"
            }

            override fun getErrorMsgKey(): String {
                return "errorMsg"
            }

            override fun getDataKey(): String {
                return "data"
            }

            override fun isRequestSuccess(statusCode: Int): Boolean {
                return statusCode == 0   //0 表示业务上是正确返回了数据
            }
        }
    }

}

