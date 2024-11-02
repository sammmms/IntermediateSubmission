package com.example.intermediatesubmission.data.retrofit

import com.example.intermediatesubmission.data.response.DetailStoryResponse
import com.example.intermediatesubmission.data.response.LoginResponse
import com.example.intermediatesubmission.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    // Authentication
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    // Story
    @GET("stories")
    fun getAllStory(): Call<StoryResponse>

    // Detail Story
    @GET("stories/{id}")
    fun getStory(@Path("id") id: String): Call<DetailStoryResponse>

    // Post Story
    @Multipart
    @POST("stories")
    fun postStory(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: RequestBody? = null,
        @Part("long") long: RequestBody? = null
    ): Call<DetailStoryResponse>
}