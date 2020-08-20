package com.mhamza007.iptv.model.user

import com.google.gson.annotations.SerializedName

data class UserInfo(

    @SerializedName("username") val username: Int,
    @SerializedName("password") val password: Int,
    @SerializedName("message") val message: String,
    @SerializedName("auth") val auth: Int,
    @SerializedName("status") val status: String,
    @SerializedName("exp_date") val exp_date: Int,
    @SerializedName("is_trial") val is_trial: Int,
    @SerializedName("active_cons") val active_cons: Int,
    @SerializedName("created_at") val created_at: Int,
    @SerializedName("max_connections") val max_connections: Int,
    @SerializedName("allowed_output_formats") val allowed_output_formats: List<String>
) {
    constructor() : this(0, 0, "", 0, "", 0, 0, 0, 0, 0, listOf<String>())
}