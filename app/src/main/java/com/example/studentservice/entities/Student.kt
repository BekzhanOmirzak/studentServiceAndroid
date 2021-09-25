package com.example.studentservice.entities

import com.google.gson.annotations.SerializedName


 class Student(var just: Int) {
    @SerializedName("name")
    var name: String = "";

    @SerializedName("lastName")
    var lastName: String = ""

    @SerializedName("email")
    var email: String = ""

    @SerializedName("password")
    var password: String = ""

    @SerializedName("major")
    var major: String = ""

    @SerializedName("phoneNumber")
    var phoneNumber: String = ""

    @SerializedName("university")
    var university: String = ""

    @SerializedName("imageLink")
    val imageLink: String = ""

    @SerializedName("imagePath")
    val imagePath: String = ""

}
