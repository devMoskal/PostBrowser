package com.dev.moskal.postbrowser.data.network.response

import com.google.gson.annotations.SerializedName

data class UserApiResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("username") val username: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("address") val address: AddressApiResponse?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("website") val website: String?,
    @SerializedName("company") val company: CompanyApiResponse?,

    )

data class AddressApiResponse(
    @SerializedName("street") val street: String?,
    @SerializedName("suite") val suite: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("zipcode") val zipcode: String?,
    @SerializedName("geo") val geo: GeoApiResponse?,
)

data class CompanyApiResponse(
    @SerializedName("name") val name: String?,
    @SerializedName("catchPhrase") val catchPhrase: String?,
    @SerializedName("bs") val bs: String?,
)

data class GeoApiResponse(
    @SerializedName("lat") val lat: Double?,
    @SerializedName("lng") val lng: Double?,
)