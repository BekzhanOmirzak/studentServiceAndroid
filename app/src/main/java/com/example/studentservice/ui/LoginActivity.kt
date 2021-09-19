package com.example.studentservice.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.studentservice.R
import com.example.studentservice.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater);
        val view = binding.root;
        setContentView(view);
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction().replace(R.id.container, LoginFragment())
                .commit();
    }


}