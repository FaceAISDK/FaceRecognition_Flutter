package com.architecture.httplib.beans

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/**
 * 不需要，不需要
 * Alt + K 能自动生成代码
 *
 */
@JsonClass(generateAdapter = true)
data class FakerBaseResponse(
    @Json(name = "code")
    val code: Int,
    @Json(name = "data")
    val `data`: List<Any>,
    @Json(name = "status")
    val status: String,
    @Json(name = "total")
    val total: Int
)