package com.example.calculapp.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.calculapp.R
import com.example.calculapp.databinding.ActivityUserRegisterBinding

class UserRegisterActivity : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding: ActivityUserRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}