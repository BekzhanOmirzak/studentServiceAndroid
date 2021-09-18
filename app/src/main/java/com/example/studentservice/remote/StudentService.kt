package com.example.studentservice.remote

import com.example.studentservice.entities.Student
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface StudentService {

    @POST("register")
    suspend fun registerStudent(@Body student: Student): String;

    @GET("login")
    suspend fun loginStudent(@Query("email") email: String, @Query("password") password: String) : Response<String>



}
