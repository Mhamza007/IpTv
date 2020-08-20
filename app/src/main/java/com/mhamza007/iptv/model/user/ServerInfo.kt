package com.mhamza007.iptv.model.user

import com.google.gson.annotations.SerializedName

data class ServerInfo(
    @SerializedName("url") val url: String,
    @SerializedName("port") val port: Int,
    @SerializedName("https_port") val https_port: Int,
    @SerializedName("server_protocol") val server_protocol: String,
    @SerializedName("rtmp_port") val rtmp_port: Int,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("timestamp_now") val timestamp_now: Int,
    @SerializedName("time_now") val time_now: String,
    @SerializedName("process") val process: Boolean
) {
    constructor() : this("", 0, 0, "", 0, "", 0, "", false)
}