package com.example.studentservice.remote

import com.example.studentservice.entities.Student
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface StudentService {

    @POST("register")
    suspend fun registerStudent(@Body student: Student): String;

    @GET("login")
    suspend fun loginStudent(
        @Query("email") email: String,
        @Query("password") password: String
    ): String

    @GET("{email}")
    suspend fun getStudentDetail(@Path("email") email: String): Student


    @POST("{email}/updatestudent")
    suspend fun updateStudent(@Path("email") email: String, @Body student: Student): Response<Void>


    @Multipart
    @POST("uploadimage")
    suspend fun uploadImage(@Part ("email") email: String, @Part file: MultipartBody.Part) : Response<Void>




}
