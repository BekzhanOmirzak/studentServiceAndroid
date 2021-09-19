package com.example.studentservice.viewmodel

import androidx.lifecycle.*
import com.example.studentservice.entities.Student
import com.example.studentservice.repository.LoginRepository
import com.example.studentservice.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) :
    ViewModel() {

    private val _registerLiveData = MutableLiveData<Resource<String>>();
    private val _loginLiveData = MutableLiveData<Resource<String>>();
    val registerLiveData: LiveData<Resource<String>> = _registerLiveData;
    val loginLiveData: LiveData<Resource<String>> = _loginLiveData;


    fun register(student: Student) {
        _registerLiveData.value = Resource.Loading();
        viewModelScope.launch {
            try {
                val response = loginRepository.register(student);
                _registerLiveData.value = Resource.Success(response);
            } catch (ex: Exception) {
                _registerLiveData.value = Resource.Error(ex.message!!);
            }
        }
    }

    fun loginByEmailPassword(email: String, password: String) {
        _loginLiveData.value = Resource.Loading(null);
        viewModelScope.launch {
            try {
                val response = loginRepository.loginByEmailPassword(email, password);
                _loginLiveData.value = Resource.Success(response);
            } catch (ex: Exception) {
                _loginLiveData.value = Resource.Error(ex.message!!, null);
            }
        }
    }





}