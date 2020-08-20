package com.mhamza007.iptv.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LiveTv {
    @SerializedName("num")
    @Expose
    var num: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("stream_type")
    @Expose
    var streamType: String? = null

    @SerializedName("stream_id")
    @Expose
    var streamId: Int? = null

    @SerializedName("stream_icon")
    @Expose
    var streamIcon: String? = null

    @SerializedName("epg_channel_id")
    @Expose
    var epgChannelId: Any? = null

    @SerializedName("added")
    @Expose
    var added: String? = null

    @SerializedName("is_adult")
    @Expose
    var isAdult: String? = null

    @SerializedName("category_id")
    @Expose
    var categoryId: String? = null

    @SerializedName("custom_sid")
    @Expose
    var customSid: String? = null

    @SerializedName("tv_archive")
    @Expose
    var tvArchive: Int? = null

    @SerializedName("direct_source")
    @Expose
    var directSource: String? = null

    @SerializedName("tv_archive_duration")
    @Expose
    var tvArchiveDuration: Int? = null

}