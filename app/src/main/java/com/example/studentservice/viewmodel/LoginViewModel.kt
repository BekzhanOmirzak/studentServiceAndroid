package com.example.studentservice.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentservice.entities.Student
import com.example.studentservice.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) :
    ViewModel() {

    private val _registerLiveData = MutableLiveData<String>();
    private val _loginLiveData = MutableLiveData<String>();
    val registerLiveData: LiveData<String> = _registerLiveData;
    val loginLiveData: LiveData<String> = _loginLiveData;


    fun register(student: Student) {
        viewModelScope.launch {
            _registerLiveData.value = loginRepository.register(student)
        }
    }

    fun loginByEmailPassword(email: String, password: String) {
        viewModelScope.launch {
            val response = loginRepository.loginByEmailPassword(email, password);
            if (response.isSuccessful) {
                _loginLiveData.value = response.body()!!
            } else {
                _loginLiveData.value = "error";
            }
        }
    }


}