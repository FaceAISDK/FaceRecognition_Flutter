package com.architecture.httplib.base

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.architecture.httplib.core.HttpResponseCallAdapterFactory
import com.architecture.httplib.core.MoshiResultTypeAdapterFactory
import com.architecture.httplib.core.MoshiResultTypeAdapterFactory.HttpWrapper
import com.architecture.httplib.interceptor.CommonRequestInterceptor
import com.architecture.httplib.interceptor.CommonResponseInterceptor
import com.architecture.httplib.error.GlobalErrorHandler
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * 网络请求，Retrofit 基础配置 BaseRetrofitConfig
 *
 *
 *
 * https://github.com/AnyLifeZLB
 * @author anylife.zlb@gmail.com
 */
// Rename BaseNetworkApiService --> BaseHttpApiService
abstract class BaseHttpApiService {

    var mRetrofit: Retrofit //C 位主角

    private val globalErrorHandler = GlobalErrorHandler()


    // object 与companion object的区别 https://www.jianshu.com/p/14db81e1576a
    companion object {
        private var iNetworkRequiredInfo: IHttpRequestBaseInfo? = null

        //Application 全局的初始化
        fun init(networkRequiredInfo: IHttpRequestBaseInfo) {
            iNetworkRequiredInfo = networkRequiredInfo
        }
    }


    //https://github.com/square/moshi 就是为了替换GSON ，详情查阅相关介绍
    private val moshi = Moshi.Builder()
         //添加返回的json 数据自定义解析器
        .add(MoshiResultTypeAdapterFactory(getHttpWrapperHandler()))
        .addLast(KotlinJsonAdapterFactory()) //
        .build()


    /**
     * 再封装一下，这样还是有点不一样
     *
     * getXXXApiService  就可以调用多方便
     *
     */
    open fun <T> getService(service: Class<T>): T {
        //Retrofit 的核心操作
        //create方法就是返回了一个Proxy.newProxyInstance动态代理对象
        return mRetrofit.create(service)
    }



    /**
     * 构造方法可以改造一下，（）init
     *
     */
    constructor(baseUrl:String) {
        val retrofitBuilder = Retrofit.Builder()
        retrofitBuilder.baseUrl(baseUrl)
        retrofitBuilder.client(getOkHttpClient())

        //1. 转换器，使用Moshi 是因为codegen 比GSON 反射更加的高效
        retrofitBuilder.addConverterFactory(MoshiConverterFactory.create(moshi))

        // 2. 就是为了返回HttpResult<T> 这个啊
        //suspend fun getFakerData(): HttpResult<List<FakerDataBean>>
        retrofitBuilder.addCallAdapterFactory(
            HttpResponseCallAdapterFactory(globalErrorHandler) //全局的错误处理器
        )

        //3. 底层使用OKHTTP
        retrofitBuilder.client(getOkHttpClient())

        mRetrofit = retrofitBuilder.build()
    }

    private fun log(msg: Any?) = println("[${Thread.currentThread().name}] $msg")


    /**
     * OkHttpClient
     *
     */
    private fun getOkHttpClient(): OkHttpClient{
        val okHttpClientBuilder = OkHttpClient.Builder()

        okHttpClientBuilder.addInterceptor(CommonRequestInterceptor(iNetworkRequiredInfo))

        //返回拦截器
        okHttpClientBuilder.addInterceptor(CommonResponseInterceptor())
        if(getInterceptor() != null) {
            okHttpClientBuilder.addInterceptor(getInterceptor()!!)
        }

        //只有在调试状态才需要打印日志
        if (iNetworkRequiredInfo != null && iNetworkRequiredInfo!!.isDebug) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)

            //通知栏打印HTTP LOG
            okHttpClientBuilder.addInterceptor(ChuckerInterceptor(iNetworkRequiredInfo!!.applicationContext ))
        } else {
            // throw exception

        }


        //默认值设置低一点，以便暴露出整个系统的问题所在。
        okHttpClientBuilder
            .retryOnConnectionFailure(true)//默认重试一次，若需要重试N次，则要实现拦截器。
            .connectTimeout(2,TimeUnit.SECONDS)
            .readTimeout(2,TimeUnit.SECONDS)
            .writeTimeout(2, TimeUnit.SECONDS)
            .callTimeout(2, TimeUnit.MINUTES)


        return okHttpClientBuilder.build()
    }


    /**
     * 不同的host 除了通用的拦截器，还可以定制化自己的拦截器
     *
     */
    protected abstract fun getInterceptor(): Interceptor?


    /**
     * 定义好字段映射关系
     * 如果能映射到code+msg+data 就去重写本方法，否则就空着
     * 然后就要自己判断业务成功code和对应的处理之类的了
     *
     */
    protected open fun getHttpWrapperHandler(): HttpWrapper? {
        return null
    }

}