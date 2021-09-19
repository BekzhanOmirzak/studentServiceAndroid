package com.example.studentservice.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.example.studentservice.R
import com.example.studentservice.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.NavigationMenuItemView
import com.google.android.material.navigation.NavigationBarView
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
                    fragment = CreateFragment();
                }
            }
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment!!)
                .commit();
            true;
        }

    }


}