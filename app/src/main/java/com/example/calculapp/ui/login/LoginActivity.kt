package com.example.calculapp.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.calculapp.R
import com.example.calculapp.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }


    //Function to init and configure user interface
    private fun initUi() {

    }


}