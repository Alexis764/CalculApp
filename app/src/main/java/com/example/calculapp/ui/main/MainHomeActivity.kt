package com.example.calculapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.calculapp.R
import com.example.calculapp.databinding.ActivityMainHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainHomeActivity : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding: ActivityMainHomeBinding

    //Val and var
    private lateinit var navController: NavController

    //Constants
    companion object {
        const val USER_IDENTIFICATION_NUMBER = "userIdentificationNumber"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }


    //Function to init and configure user interface
    private fun initUi() {
        initNavigation()
    }


    //Function to init and configure main navigation
    private fun initNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragment_container_view_tag) as NavHostFragment
        navController = navHost.navController
        binding.bottomNavigation.setupWithNavController(navController)
    }


}