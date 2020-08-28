package com.mhamza007.iptv.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MovieCategory {
    @SerializedName("category_id")
    @Expose
    var categoryId: String? = null

    @SerializedName("category_name")
    @Expose
    var categoryName: String? = null

    @SerializedName("parent_id")
    @Expose
    var parentId: Int? = null
}