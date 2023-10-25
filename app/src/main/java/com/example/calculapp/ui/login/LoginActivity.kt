package com.example.calculapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.calculapp.data.preference.KeepLogin
import com.example.calculapp.data.sqlite.UserCreditsDataBase
import com.example.calculapp.databinding.ActivityLoginBinding
import com.example.calculapp.ui.credit.AskCreditFragment
import com.example.calculapp.ui.main.MainHomeActivity
import com.example.calculapp.ui.main.MainHomeActivity.Companion.USER_IDENTIFICATION_NUMBER
import com.example.calculapp.ui.register.UserRegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding: ActivityLoginBinding

    //Database
    private val userCreditsDataBase = UserCreditsDataBase(this)

    //Preference
    private val keepLogin = KeepLogin(this)

    //Val and var
    private var valueMoney: Int = 0
    private var valueDays: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }


    //Function to init and configure user interface
    private fun initUi() {
        initExtras()
        initListeners()
    }


    //Function to recover extras
    private fun initExtras() {
        valueMoney = intent.extras?.getInt(AskCreditFragment.EXT_AMOUNT_REQUESTED) ?: 0
        valueDays = intent.extras?.getInt(AskCreditFragment.EXT_DAYS) ?: 0
    }


    //Function to init click listeners
    private fun initListeners() {
        binding.btnLogIn.setOnClickListener {
            val userEmail = binding.tietEmail.text.toString()
            val userPassword = binding.tietPassword.text.toString()

            if (userEmail.trim().isNotEmpty() && userPassword.trim().isNotEmpty()) {
                val userIdentificationNumber = userCreditsDataBase.userLogin(userEmail, userPassword)
                if (userIdentificationNumber != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        keepLogin.saveUserIdentificationNumber(userIdentificationNumber)
                        keepLogin.saveAutoLogin(true)
                    }
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
        finish()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, UserRegisterActivity::class.java)
        intent.putExtra(AskCreditFragment.EXT_AMOUNT_REQUESTED, valueMoney)
        intent.putExtra(AskCreditFragment.EXT_DAYS, valueDays)
        startActivity(intent)
        finish()
    }


}