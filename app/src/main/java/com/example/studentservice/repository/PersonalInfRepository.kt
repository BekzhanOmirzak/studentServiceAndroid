package com.example.studentservice.repository

import com.example.studentservice.entities.Student
import com.example.studentservice.remote.StudentService
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import javax.inject.Inject

class PersonalInfRepository @Inject constructor(
    private val studentService: StudentService
) {

    suspend fun getStudentByEmail(email: String) = studentService.getStudentDetail(email);

    suspend fun updateStudent(oldEmail: String, student: Student) =
        studentService.updateStudent(oldEmail, student);


    suspend fun uploadImage(email:String,part:MultipartBody.Part)=studentService.uploadImage(email,part);

}