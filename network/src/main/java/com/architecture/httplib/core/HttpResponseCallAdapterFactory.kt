package com.architecture.httplib.core

import com.architecture.httplib.error.BusinessException
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 作用：将默认的网络请求执行器（OkHttpCall）转换成适合被不同平台来调用的网络请求执行器形式
 *
 * 在Retrofit中提供了四种CallAdapterFactory： Executor（默认）、Guava 、Java8 、RxJava
 *
 *
 *
 * extends CallAdapter
 *
 * Call<T>  --> HttpResponseCall<T>
 * 就是为了返回HttpResult<T>
 *
 * suspend fun getFakerData(): HttpResult<List<FakerDataBean>>
 *
 * https://github.com/AnyLifeZLB
 * @author anylife.zlb@gmail.com
 *
 */
class HttpResponseCallAdapterFactory(private val errorHandler: ErrorHandler? = null) :
    CallAdapter.Factory() {

    /**
     * [onFailure] will be called when [Result.isFailure]
     *
     */
    fun interface ErrorHandler {
        fun onFailure(throwable: BusinessException)
    }

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        // suspend functions wrap the response type in `Call`
        if (Call::class.java != getRawType(returnType)) {
            return null
        }

        // check first that the return type is `ParameterizedType`
        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<HttpResult<<Foo>> or Call<HttpResult<out Foo>>"
        }


        // get the response type inside the `Call` type
        val responseType = getParameterUpperBound(0, returnType)

        // 类型检查
        // if the response type is not ApiResponse then we can't handle this type, so we return null
        if (getRawType(responseType) != HttpResult::class.java) {
            return null
        }

        // the response type is ApiResponse and should be parameterized
        check(responseType is ParameterizedType) { "Response must be parameterized as HttpResult<Foo> or HttpResponse<out Foo>" }

        // 上面都是一些基本的参数检查，类型匹配

        // 这里才是主角，Call<Any>   ---> HttpResponseCall
        return object : CallAdapter<Any, Call<*>?> {

            override fun responseType(): Type {
                return responseType
            }

            override fun adapt(call: Call<Any>): Call<*> {
                // suspend fun getFakerData(): HttpResult<List<FakerDataBean>>
                // 就是为了返回HttpResult<T> 这个啊
                return HttpResponseCall(call, errorHandler)
            }

        }

    }
}
