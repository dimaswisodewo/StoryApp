package com.dicoding.storyapp.networking

import com.dicoding.storyapp.model.GeneralResponse
import com.dicoding.storyapp.model.LoginResponse
import com.dicoding.storyapp.model.StoriesResponse
import com.dicoding.storyapp.model.StoryDetailResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<GeneralResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<LoginResponse>

    @POST("stories")
    fun addNewStory(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ) : Call<GeneralResponse>

    @POST("stories/guest")
    fun addNewStoryGuest(
        @Body requestBody: RequestBody
    ) : Call<GeneralResponse>

    @GET("stories")
    fun getAllStories(
        @HeaderMap headers: Map<String, String>,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int = 0
    ) : Call<StoriesResponse>

    @GET("/stories/:{id}")
    fun getDetailStory(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id: String
    ) : Call<StoryDetailResponse>

    fun getHeaderAddStory() : Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["Content-Type"] = "multipart/form-data"
        headerMap["Authorization"] = ApiConfig.USER.token
        return headerMap
    }

    fun getHeaderAuthorization() : Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["Authorization"] = ApiConfig.USER.token
        return headerMap
    }
}