package com.example.calculapp.ui.registercredit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.calculapp.R
import com.example.calculapp.databinding.ActivityRegisterCreditBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterCreditActivity : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding: ActivityRegisterCreditBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterCreditBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}