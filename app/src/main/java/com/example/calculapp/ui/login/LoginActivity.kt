package com.example.calculapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.calculapp.data.sqlite.UserCreditsDataBase
import com.example.calculapp.databinding.ActivityLoginBinding
import com.example.calculapp.ui.credit.AskCreditFragment
import com.example.calculapp.ui.main.MainHomeActivity
import com.example.calculapp.ui.main.MainHomeActivity.Companion.USER_IDENTIFICATION_NUMBER
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding: ActivityLoginBinding

    //Database
    private val userCreditsDataBase = UserCreditsDataBase(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }


    //Function to init and configure user interface
    private fun initUi() {
        initListeners()
    }


    //Function to init click listeners
    private fun initListeners() {
        binding.btnLogIn.setOnClickListener {
            val userEmail = binding.tietEmail.text.toString()
            val userPassword = binding.tietPassword.text.toString()

            if (userEmail.trim().isNotEmpty() && userPassword.trim().isNotEmpty()) {
                val userIdentificationNumber = userCreditsDataBase.userLogin(userEmail, userPassword)
                if (userIdentificationNumber != null) {
                    startMainHomeActivity(userIdentificationNumber)

                } else {
                    Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }


    //Function to start MainHomeActivity and send necessary data
    private fun startMainHomeActivity(userIdentificationNumber: Long) {
        val intent = Intent(this, MainHomeActivity::class.java)
        intent.putExtra(USER_IDENTIFICATION_NUMBER, userIdentificationNumber)
        intent.putExtra(AskCreditFragment.EXT_AMOUNT_REQUESTED, 250000)
        intent.putExtra(AskCreditFragment.EXT_DAYS, 10)
        startActivity(intent)
    }


}