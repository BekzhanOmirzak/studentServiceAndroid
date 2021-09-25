package com.example.studentservice.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences


object TempStorage {

    private lateinit var preferences: SharedPreferences;

    fun init(activity: Activity) {
        preferences = activity.getPreferences(Context.MODE_PRIVATE);
    }

    fun saveEmail(email: String) {
        with(preferences.edit()) {
            putString("email", email);
            apply();
        }
    }

    fun getEmail(): String {
        return preferences.getString("email","no")!!;
    }






}