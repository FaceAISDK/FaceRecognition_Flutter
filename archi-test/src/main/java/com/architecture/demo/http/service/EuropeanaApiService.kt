package com.architecture.demo.http.service

import com.architecture.demo.http.myinterface.EuropeanaApi
import com.architecture.demo.http.myinterface.ExceptionApi
import com.architecture.httplib.core.MoshiResultTypeAdapterFactory
import com.architecture.httplib.base.BaseHttpApiService
import okhttp3.Interceptor

/**
 * EuropeanaApiService 就是无法映射Http Base Wrapper Data，这里就只能自己判断数据是否成功了
 *
 */
object EuropeanaApiService : BaseHttpApiService("https://api.europeana.eu/record/v2/") {

    override fun getInterceptor(): Interceptor {
        return Interceptor { chain ->
            val builder = chain.request().newBuilder()
            chain.proceed(builder.build())
        }
    }


    /**
     *  Europeana ApiService 返回的不是标准code+msg+data HttpWrapper包装类
     *
     * @return
     */
    override fun getHttpWrapperHandler(): MoshiResultTypeAdapterFactory.HttpWrapper? {
        //return null
        return super.getHttpWrapperHandler()
    }


    /**
     * 减少一点重复代码
     *
     */
    fun getService(): EuropeanaApi {
        return super.getService(EuropeanaApi::class.java)
    }


}

