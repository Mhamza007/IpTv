package com.mhamza007.iptv.model.user

import com.google.gson.annotations.SerializedName

data class Base(
	@SerializedName("user_info") val user_info: UserInfo,
	@SerializedName("server_info") val server_info: ServerInfo
)