package com.example.calculapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.example.calculapp.R
import com.example.calculapp.data.preference.KeepLogin
import com.example.calculapp.databinding.ActivityLoadBinding
import com.example.calculapp.ui.about.AboutActivity
import com.example.calculapp.ui.credit.AskCreditFragment
import com.example.calculapp.ui.main.MainHomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoadActivity : AppCompatActivity() {

    //Binding
    private lateinit var binding: ActivityLoadBinding

    //Val and var
    private lateinit var countTimerSplash: CountDownTimer
    private lateinit var intent: Intent

    //Preference
    private val keepLogin = KeepLogin(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()

    }


    //Function to init and configure user interface
    private fun initUi() {
        initNextScreen()
        initCountTimerSplash()
    }


    //Function to prepare next screen according autologin
    private fun initNextScreen() {
        CoroutineScope(Dispatchers.IO).launch {
            keepLogin.getAutoLogin().collect{ keepLoginModel ->
                if (keepLoginModel.logging) {
                    intent = Intent(binding.tvCount.context, MainHomeActivity::class.java)
                    intent.putExtra(MainHomeActivity.USER_IDENTIFICATION_NUMBER, keepLoginModel.userIdentificationNumber)
                    intent.putExtra(AskCreditFragment.EXT_AMOUNT_REQUESTED, 250000)
                    intent.putExtra(AskCreditFragment.EXT_DAYS, 10)

                } else {
                    intent = Intent(binding.tvCount.context, AboutActivity::class.java)
                }
            }
        }
    }


    /*Function to init count timer to splashScreen duration
    * Duration = 3 seconds
    * Tick = 1 second
    * Finish = Next activity (AboutActivity)*/
    private fun initCountTimerSplash() {
        countTimerSplash = object : CountDownTimer(3000, 1000) {
            override fun onTick(time: Long) {
                val seconds = time / 1000
                binding.tvCount.text = getString(R.string.secondsText, seconds)
            }

            override fun onFinish() {
                startActivity(intent)
                finish()
            }

        }

        countTimerSplash.start()
    }


}