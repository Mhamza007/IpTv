package com.mhamza007.iptv.api

import com.mhamza007.iptv.model.LiveTv
import com.mhamza007.iptv.model.LiveTvCategory
import com.mhamza007.iptv.model.Movie
import com.mhamza007.iptv.model.MovieCategory
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

//    @POST("player_api.php?username=&password=")
//    fun loginWithActCode(
//        @Query("password") actCode: String
//    ): Call<UserInfo>

    @GET("player_api.php?username=&password=")
    fun getUserInfo(
        @Query("username") username: String,
        @Query("password") password: String
    ): Call<UserInfo>

    // Get Live TV Categories List
    @GET("player_api.php?username=&password=&action=")
    fun getLiveStreamsCategories(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String
    ): Call<List<LiveTvCategory>>

    // Get All Live Tv List
    @GET("player_api.php?username=&password=&action=")
    fun getLiveStreams(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String
    ): Call<List<LiveTv>>

    // Load Live Tvs from Category Id
    @GET("player_api.php?username=&password=&action=&category_id=")
    fun getLiveStreamsFromCategoryId(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String,
        @Query("category_id") categoryId: String
    ): Call<List<LiveTv>>

    // Get Movies Categories List
    @GET("player_api.php?username=&password=&action=")
    fun getMoviesCategories(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String
    ): Call<List<MovieCategory>>

    // Get Movies List
    @GET("player_api.php?username=&password=&action=")
    fun getMovies(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String
    ): Call<List<Movie>>

    // Get Movies List by Category Id
    @GET("player_api.php?username=&password=&action=&category_id=")
    fun getMoviesFromCategoryId(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String,
        @Query("category_id") categoryId: String
    ): Call<List<Movie>>
}