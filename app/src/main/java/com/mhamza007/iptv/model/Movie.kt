package com.mhamza007.iptv.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Movie {
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
    var streamIcon: Any? = null

    @SerializedName("rating")
    @Expose
    var rating: Any? = null

    @SerializedName("rating_5based")
    @Expose
    var rating5based: Int? = null

    @SerializedName("added")
    @Expose
    var added: String? = null

    @SerializedName("category_id")
    @Expose
    var categoryId: String? = null

    @SerializedName("container_extension")
    @Expose
    var containerExtension: String? = null

    @SerializedName("custom_sid")
    @Expose
    var customSid: Any? = null

    @SerializedName("direct_source")
    @Expose
    var directSource: String? = null
}