package com.architecture.demo.http.beans

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/**
 * Moshi+codegen
 *
 *
 */
@JsonClass(generateAdapter = true)
data class EuropeanaBean(

    @Json(name = "success")
    val success: Boolean, //是否成功

    @Json(name = "itemsCount")
    val itemsCount: Int, //item 个数

    @Json(name = "items")
    val items: List<Item> //item 内容

    // 剩下的字段丢弃，为了方便演示
)


@JsonClass(generateAdapter = true)
data class Item(
    @Json(name = "dataProvider")
    val dataProvider: List<String>

    // 剩下的字段丢弃，为了方便演示

)