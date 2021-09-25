package com.example.studentservice.repository

import com.example.studentservice.remote.PostService
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val postService: PostService
) {

    suspend fun sendPostToServer(
        email: RequestBody,
        subject: RequestBody,
        content: RequestBody,
        image: MultipartBody.Part
    ) = postService.createPost(email, subject, content, image);

}