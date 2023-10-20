package com.example.calculapp.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.calculapp.R
import com.example.calculapp.databinding.ActivityLoadBinding
import com.example.calculapp.ui.about.AboutActivity

class LoadActivity : AppCompatActivity() {

    //Binding
    private lateinit var binding: ActivityLoadBinding

    //Val and var
    private lateinit var countTimerSplash: CountDownTimer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()

    }


    //Function to init and configure user interface
    private fun initUi() {
        initCountTimerSplash()
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
                val intent = Intent(binding.tvCount.context, AboutActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

        countTimerSplash.start()
    }


}