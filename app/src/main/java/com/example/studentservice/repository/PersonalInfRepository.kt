package com.example.studentservice.repository

import com.example.studentservice.entities.Student
import com.example.studentservice.remote.StudentService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Multipart
import javax.inject.Inject

class PersonalInfRepository @Inject constructor(
    private val studentService: StudentService
) {

    suspend fun getStudentByEmail(email: String) = studentService.getStudentDetail(email);

    suspend fun updateStudent(oldEmail: String, student: Student) =
        studentService.updateStudent(oldEmail, student);


    suspend fun uploadPhoto(email: RequestBody, img: MultipartBody.Part) =
        studentService.uploadPhoto(email, img);


    suspend fun downloadPhoto(imagePath: String, imageLink: String) =
        studentService.downloadPhoto(imagePath, imageLink);

    suspend fun getListOfImageFiles(email: String) = studentService.downloadImageFileList(email);


}