package com.example.calculapp.ui.about

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calculapp.databinding.ActivityAboutBinding
import com.example.calculapp.ui.registercredit.RegisterCreditActivity

class AboutActivity : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding: ActivityAboutBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()

    }


    //Function to init and configure user interface
    private fun initUi() {
        initListeners()
    }


    /*Function to init click components
    * btnNext = Next activity(RegisterCreditActivity)*/
    private fun initListeners() {
        binding.btnNext.setOnClickListener {
            val intent = Intent(this, RegisterCreditActivity::class.java)
            startActivity(intent)
        }
    }


}