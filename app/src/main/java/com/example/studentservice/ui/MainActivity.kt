package com.example.studentservice.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.studentservice.R
import com.example.studentservice.databinding.ActivityMainBinding
import com.example.studentservice.util.TempStorage
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        val view = binding.root;
        setContentView(view);
        handleBottomNavViewClick();
        firstFragment();
        //temporarily
        TempStorage.init(this);
    }

    private fun firstFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.container, PostFragment())
            .commit();
    }


    private fun handleBottomNavViewClick() {

        binding.bottomMenu.setOnItemSelectedListener { item ->
            var fragment: Fragment? = null;
            when (item.itemId) {
                R.id.posts -> {
                    fragment = PostFragment();
                }
                R.id.profile -> {
                    fragment = PersonalFragment();
                }
                R.id.create -> {
                    fragment = CreatePostFragment();
                }
            }
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment!!)
                .commit();
            true;
        }

    }


}