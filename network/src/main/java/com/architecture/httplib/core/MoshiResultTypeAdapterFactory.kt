package com.architecture.httplib.core

import com.squareup.moshi.*
import com.architecture.httplib.error.BusinessException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * : JsonAdapter.Factory
 * 要把数据能适配到T上面来。比如 NewsChannelsBean
 *
 * Converts Java values to JSON, and JSON values to Java.
 *
 *  @GET("release/channel")
 *  suspend fun getNewsChannelsWithoutEnvelope(): HttpResponse<NewsChannelsBean>
 *
 * 视频 1：30 about  90min
 */
class MoshiResultTypeAdapterFactory(private val httpWrapper: HttpWrapper?) : JsonAdapter.Factory {

    /**
     * HttpResultWrapper
     *
     * 假设公司各个业务服务器返回的Http Json数据格式都差不多三个大字段（名称允许自定义不同）code[int] + msg[str] +data[T]
     * 这里我们统一约定称含有三个类似字段为包装数据wrapper data,每个Http请求正常都会含有这三个字段，不同的是data 中的数据，
     * 很自然的我们使用范型T 来表示。
     *
     * 根据项目自身的情况再次拓展一下让其更具有包容性
     *
     */
    interface HttpWrapper {
        fun getStatusCodeKey(): String
        fun getErrorMsgKey(): String
        fun getDataKey(): String       //一般会命名为result data

        //有些服务器是0代表成功，有些是200 代表成功，我司的java 和Python 后台就没有统一过
        //这里的成功是指业务请求的成功，请和Http response Code 区分
        fun isRequestSuccess(statusCode: Int): Boolean
    }

    override fun create(
        type: Type,
        annotations: MutableSet<out Annotation>,
        moshi: Moshi
    ): JsonAdapter<*>? {

        val rawType = type.rawType

        if (rawType != HttpResult::class.java) return null

        val dataType: Type =
            (type as? ParameterizedType)?.actualTypeArguments?.firstOrNull() ?: return null

        val dataTypeAdapter = moshi.nextAdapter<Any>(this, dataType, annotations)

        //Result<T> 范型解析出来
        return ResultTypeAdapter(dataTypeAdapter, httpWrapper)
    }


    /**
     * 返回请求需求的那个T
     *
     */
    class ResultTypeAdapter<T>(
        private val dataTypeAdapter: JsonAdapter<T>,
        private val httpWrapper: HttpWrapper?
    ) : JsonAdapter<T>() {

        /**
         * Decodes a nullable instance of type T from the given reader.
         *
         */
        override fun fromJson(reader: JsonReader): T? {

            if (httpWrapper != null) {
                reader.beginObject()

                //一般都是code +msg + result/data

                var errcode: Int? = null
                var msg: String? = null
                var data: Any? = null

                while (reader.hasNext()) {
                    when (reader.nextName()) {
                        //根据不同服务器后台HTTP 报文字段 解析映射出code +msg + data
                        httpWrapper.getStatusCodeKey() -> errcode = reader.nextString().toIntOrNull()
                        httpWrapper.getErrorMsgKey() -> msg = reader.nextString()
                        httpWrapper.getDataKey() -> data = dataTypeAdapter.fromJson(reader)
                        else -> reader.skipValue()
                    }
                }

                reader.endObject()

                // 这个字段要看看是否服务器是否是必传字段
                // 否则没有必要抛异常
                if (errcode == null)
                    throw JsonDataException("Expected field [err code] not present.")

                if (httpWrapper.isRequestSuccess(errcode)){
                    return data as T
                }else{
                    throw BusinessException(errcode, msg)
                }

            } else {
                //envelope == null 不是标准的Code + msg +data 也没关系
                return dataTypeAdapter.fromJson(reader) as T
            }

        }



        /**
         * Encodes the given value with the given writer.
         * 后面再说吧
         *
         */
        override fun toJson(writer: JsonWriter, value: T?): Unit = TODO("Not yet implemented")



    }

}
