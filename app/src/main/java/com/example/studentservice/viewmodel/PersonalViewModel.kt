package com.example.studentservice.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentservice.entities.ImageFile
import com.example.studentservice.entities.Student
import com.example.studentservice.repository.PersonalInfRepository
import com.example.studentservice.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.net.HttpURLConnection
import javax.inject.Inject


@HiltViewModel
class PersonalViewModel @Inject constructor(
    private val personalInfRepository: PersonalInfRepository
) : ViewModel() {

    private val _studentLiveData = MutableLiveData<Resource<Student>>();
    private val _updatingStudentsDetail = MutableLiveData<Boolean>();
    private val _gettingImageByteArray = MutableLiveData<Resource<ByteArray>>();
    private val _gettingListImageFiles = MutableLiveData<Resource<List<ImageFile>>>();
    val studentLiveData: LiveData<Resource<Student>> = _studentLiveData;
    val updatingStudentsDetail: LiveData<Boolean> = _updatingStudentsDetail;
    val gettingImageByteArray: LiveData<Resource<ByteArray>> = _gettingImageByteArray;
    val gettingListImageFiles: LiveData<Resource<List<ImageFile>>> = _gettingListImageFiles;


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
        _updatingStudentsDetail.value = true;
        viewModelScope.launch {
            val response = personalInfRepository.updateStudent(oldEmail, student);
            Log.e("Bekzhan save", response.message());
            _updatingStudentsDetail.value = false;
        }
    }


    fun uploadPhoto(email: RequestBody, img: MultipartBody.Part) {
        _updatingStudentsDetail.value = true;
        viewModelScope.launch {
            val response = personalInfRepository.uploadPhoto(email, img);
            Log.e("Bekzhan response", response);
            _updatingStudentsDetail.value = false;
        }
    }

    fun downloadPhoto(imageLink: String, imagePath: String) {
        _gettingImageByteArray.value = Resource.Loading();
        viewModelScope.launch {
            try {
                val response = personalInfRepository.downloadPhoto(imageLink, imagePath);
                _gettingImageByteArray.value = Resource.Success(response.bytes());
            } catch (ex: Exception) {
                _gettingImageByteArray.value = Resource.Error(ex.localizedMessage);
            }
        }
    }

    fun getListOfImageFiles(email: String) {
        _gettingListImageFiles.value = Resource.Loading();
        viewModelScope.launch {

            try {
                val imageFiles = personalInfRepository.getListOfImageFiles(email);
                Log.e("Bekzhan image files", "getListOfImageFiles:  $imageFiles")
                _gettingListImageFiles.value = Resource.Success(imageFiles);
            } catch (ex: Exception) {
                _gettingListImageFiles.value = Resource.Error(ex.localizedMessage);
            }
        }
    }


}