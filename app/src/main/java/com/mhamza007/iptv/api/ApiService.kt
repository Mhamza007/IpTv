package com.mhamza007.iptv.api

import com.mhamza007.iptv.model.LiveTv
import com.mhamza007.iptv.model.user.UserInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("player_api.php?username=&password=")
    fun loginWithUsername(
        @Query("username") username: String,
        @Query("password") password: String
    ): Call<UserInfo>

    @GET("player_api.php?username=&password=&action=")
    fun getLiveStreams(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String
    ): Call<List<LiveTv>>
}