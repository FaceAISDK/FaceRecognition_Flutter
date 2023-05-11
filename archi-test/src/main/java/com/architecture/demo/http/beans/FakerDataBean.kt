package com.architecture.demo.http.beans
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json



@JsonClass(generateAdapter = true)
data class FakerDataBean(
    @Json(name = "addresses")
    val addresses: List<Addresse>,
    @Json(name = "contact")
    val contact: Contact,
    @Json(name = "country")
    val country: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "image")
    val image: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "phone")
    val phone: String,
    @Json(name = "vat")
    val vat: String,
    @Json(name = "website")
    val website: String
) {
    @JsonClass(generateAdapter = true)
    data class Addresse(
        @Json(name = "buildingNumber")
        val buildingNumber: String,
        @Json(name = "city")
        val city: String,
        @Json(name = "country")
        val country: String,
        @Json(name = "county_code")
        val countyCode: String,
        @Json(name = "id")
        val id: Int,
        @Json(name = "latitude")
        val latitude: Double,
        @Json(name = "longitude")
        val longitude: Double,
        @Json(name = "street")
        val street: String,
        @Json(name = "streetName")
        val streetName: String,
        @Json(name = "zipcode")
        val zipcode: String
    )

    @JsonClass(generateAdapter = true)
    data class Contact(
        @Json(name = "address")
        val address: Address,
        @Json(name = "birthday")
        val birthday: String,
        @Json(name = "email")
        val email: String,
        @Json(name = "firstname")
        val firstname: String,
        @Json(name = "gender")
        val gender: String,
        @Json(name = "id")
        val id: Int,
        @Json(name = "image")
        val image: String,
        @Json(name = "lastname")
        val lastname: String,
        @Json(name = "phone")
        val phone: String,
        @Json(name = "website")
        val website: String
    ) {
        @JsonClass(generateAdapter = true)
        data class Address(
            @Json(name = "buildingNumber")
            val buildingNumber: String,
            @Json(name = "city")
            val city: String,
            @Json(name = "country")
            val country: String,
            @Json(name = "county_code")
            val countyCode: String,
            @Json(name = "id")
            val id: Int,
            @Json(name = "latitude")
            val latitude: Double,
            @Json(name = "longitude")
            val longitude: Double,
            @Json(name = "street")
            val street: String,
            @Json(name = "streetName")
            val streetName: String,
            @Json(name = "zipcode")
            val zipcode: String
        )
    }
}