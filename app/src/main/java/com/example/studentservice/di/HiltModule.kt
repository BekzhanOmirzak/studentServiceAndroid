package com.example.studentservice.di

import androidx.annotation.ChecksSdkIntAtLeast
import com.example.studentservice.remote.PostService
import com.example.studentservice.remote.StudentService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.internal.GsonBuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder()
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .writeTimeout(60L, TimeUnit.SECONDS)
        .build();

    @Provides
    fun provideGson() = GsonBuilder().setLenient().create();


    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson) = Retrofit.Builder()
        .baseUrl("https://service-student.herokuapp.com/api/v1/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build();


    @Provides
    @Singleton
    fun provideStudentService(retrofit: Retrofit) = retrofit.create(StudentService::class.java)

    @Provides
    @Singleton
    fun providePostService(retrofit: Retrofit) = retrofit.create(PostService::class.java)





}