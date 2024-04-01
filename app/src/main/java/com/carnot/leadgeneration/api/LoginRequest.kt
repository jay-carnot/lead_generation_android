package com.carnot.leadgeneration.api

import com.google.gson.annotations.SerializedName

data class LoginRequest(val username: String, val password: String)



data class Record(
    @SerializedName("createdTime") val createdTime: String,
    @SerializedName("fields") val fields: Fields,
    @SerializedName("id") val id: String
)

data class Fields(
    @SerializedName("Username") val userName: String,
    @SerializedName("Password") val password: String,
    @SerializedName("User ID") val userID: String // Marked as nullable as it's optional in JSON
)

data class LoginResponse(
    @SerializedName("records") val records: List<Record>
)