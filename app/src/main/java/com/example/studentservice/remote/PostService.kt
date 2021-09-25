package com.example.studentservice.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface PostService {


    @POST("createPost")
    @Multipart
    suspend fun createPost(
        @Part("email") email: RequestBody,
        @Part("subject") subject: RequestBody,
        @Part("content") content: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<Void>;






}