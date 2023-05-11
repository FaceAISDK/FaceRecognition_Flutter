package com.architecture.httplib.beans

import com.squareup.moshi.Json

/**
 * 仅仅用来处理数据转换
 *
 *
 * 一般都是 code + msg +data(/result)
 *
 * Alt + K 能自动生成代码
 *
 * 准备使用第三方的开源API 来调试数据
 *
 */
open class BusinessBaseResponse {

    //0 代表成功 ，其他代表错误。这个Code 是我们业务定义的和HTTP定义的200，300，400 是不一样的处理方式
    @Json(name = "code")
    val code: Int = 0

    @Json(name = "message")
    val message: String = ""

}