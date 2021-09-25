package com.example.studentservice.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentservice.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _sending_post = MutableLiveData<Boolean>();
    val sending_post: LiveData<Boolean> = _sending_post;


    fun sendingPost(
        email: RequestBody,
        subject: RequestBody,
        content: RequestBody,
        part: MultipartBody.Part
    ) {
        _sending_post.value = true;
        viewModelScope.launch {
            val response = postRepository.sendPostToServer(email, subject, content, part);
            Log.e("Bekzhan Response", "sendingPost:  $response  ")
            if (response.isSuccessful) {
                Log.e("Response", "sendingPost: $response")
            }
            _sending_post.value = false;
        }


    }


}