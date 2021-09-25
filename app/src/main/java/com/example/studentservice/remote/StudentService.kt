package com.example.studentservice.remote

import com.example.studentservice.entities.ImageFile
import com.example.studentservice.entities.Student
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
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
    suspend fun getStudentDetail(@Path("email") email: String): Student;


    @POST("{email}/updatestudent")
    suspend fun updateStudent(@Path("email") email: String, @Body student: Student): Response<Void>


    @POST("uploadPhoto")
    @Multipart
    suspend fun uploadPhoto(
        @Part("email") email: RequestBody,
        @Part image: MultipartBody.Part
    ): String;

    @GET("downloadPhoto")
    suspend fun downloadPhoto(
        @Query("imagePath") imagePath: String,
        @Query("imageLink") imageLink: String
    ): ResponseBody;


    @GET("listPhotos")
    suspend fun downloadImageFileList(@Query("email") email: String): List<ImageFile>;


}