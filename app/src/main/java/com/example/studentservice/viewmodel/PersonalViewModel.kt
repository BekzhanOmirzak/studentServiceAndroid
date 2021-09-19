package com.example.studentservice.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentservice.entities.Student
import com.example.studentservice.repository.PersonalInfRepository
import com.example.studentservice.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.net.HttpURLConnection
import javax.inject.Inject


@HiltViewModel
class PersonalViewModel @Inject constructor(
    private val personalInfRepository: PersonalInfRepository
) : ViewModel() {

    private val _studentLiveData = MutableLiveData<Resource<Student>>();
    val studentLiveData: LiveData<Resource<Student>> = _studentLiveData;


    fun getStudentDetailByEmail(email: String) {
        _studentLiveData.value = Resource.Loading(null);
        viewModelScope.launch {
            try {
                val response = personalInfRepository.getStudentByEmail(email);
                _studentLiveData.value = Resource.Success(response);
            } catch (ex: Exception) {
                _studentLiveData.value = Resource.Error(ex.toString());
                Log.e("Bekzhan personal", ex.message.toString());
            }
        }
    }

    fun updateStudent(oldEmail: String, student: Student) {
        viewModelScope.launch {
            val response = personalInfRepository.updateStudent(oldEmail, student);
            Log.e("Bekzhan save", response.message());
        }
    }

    fun uploadPhoto(email: String, part: MultipartBody.Part) {
        viewModelScope.launch {
            val response = personalInfRepository.uploadImage(email, part);
            Log.e("Bekzhan TAG", "uploadPhoto: ${response}")
        }
    }


}