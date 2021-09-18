package com.example.studentservice.repository

import com.example.studentservice.entities.Student
import com.example.studentservice.remote.StudentService
import javax.inject.Inject


class LoginRepository @Inject constructor(val studentService: StudentService) {

    suspend fun register(student: Student) = studentService.registerStudent(student);

    suspend fun loginByEmailPassword(email: String, password: String) =
        studentService.loginStudent(email, password);


}