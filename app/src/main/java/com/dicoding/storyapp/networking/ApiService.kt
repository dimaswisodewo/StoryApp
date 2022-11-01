package com.dicoding.storyapp.networking

import com.dicoding.storyapp.model.GeneralResponse
import com.dicoding.storyapp.model.LoginResponse
import com.dicoding.storyapp.model.StoriesResponse
import com.dicoding.storyapp.model.StoryDetailResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File

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
        @Field("description") description: String,
        @Field("photo") photo: File,
        @Field("lat") lat: Float? = null,
        @Field("lon") lon: Float? = null
    ) : Call<GeneralResponse>

    @POST("stories/guest")
    fun addNewStoryGuest(
        @Field("description") description: String,
        @Field("photo") photo: File,
        @Field("lat") lat: Float? = null,
        @Field("lon") lon: Float? = null
    ) : Call<GeneralResponse>

    @GET("stories")
    fun getAllStories(
        @HeaderMap headers: Map<String, String>,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int = 0
    ) : Call<StoriesResponse>

    @GET("/stories/:{id}")
    fun getDetailStory(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id: String
    ) : Call<StoryDetailResponse>

    fun getHeaderAddStory(userToken: String) : Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["Content-Type"] = "multipart/form-data"
        headerMap["Authorization"] = userToken
        return headerMap
    }

    fun getHeaderAuthorization(userToken: String) : Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["Authorization"] = userToken
        return headerMap
    }
}