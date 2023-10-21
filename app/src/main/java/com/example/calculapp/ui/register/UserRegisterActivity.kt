package com.example.calculapp.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.calculapp.R
import com.example.calculapp.databinding.ActivityUserRegisterBinding
import com.example.calculapp.domain.credit.CalculateCredit
import com.example.calculapp.ui.credit.AskCreditFragment.Companion.EXT_AMOUNT_REQUESTED
import com.example.calculapp.ui.credit.AskCreditFragment.Companion.EXT_DAYS
import com.example.calculapp.ui.registercredit.RegisterCreditActivity
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Currency

@AndroidEntryPoint
class UserRegisterActivity : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding: ActivityUserRegisterBinding

    //Val and var
    private lateinit var moneyFormat: NumberFormat
    private var valueMoney: Int = 0
    private var valueDays: Int = 0
    private val calculateCredit = CalculateCredit()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }


    //Function to init and configure user interface
    private fun initUi() {
        initExtras()
        initFormat()
        setUiData()
        initListeners()
    }


    //Function to recover extras
    private fun initExtras() {
        valueMoney = intent.extras?.getInt(EXT_AMOUNT_REQUESTED) ?: 0
        valueDays = intent.extras?.getInt(EXT_DAYS) ?: 0
    }


    //Function to init money format
    private fun initFormat() {
        moneyFormat = NumberFormat.getCurrencyInstance()
        moneyFormat.maximumFractionDigits = 0
        moneyFormat.currency = Currency.getInstance("USD")
    }


    //Function to load credit data
    private fun setUiData() {
        val calculatedCreditModel = calculateCredit.calculateDataCredit(valueMoney, valueDays)

        val payDay = if (calculatedCreditModel.daysRequested < 30) {
            calculatedCreditModel.creditDate.plusDays(calculatedCreditModel.daysRequested.toLong())

        } else {
            calculatedCreditModel.creditDate.plusDays(30)
        }
        val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")


        binding.tvAmountToRequest.text = moneyFormat.format(calculatedCreditModel.amountRequested)
        binding.tvPayDate.text = payDay.format(dateFormat)
        binding.tvTotalPay.text = moneyFormat.format(calculatedCreditModel.total)
    }


    //Function to init click listeners
    private fun initListeners() {
        binding.btnEdit.setOnClickListener {
            startRegisterCreditActivity()
        }
        binding.btnLogin.setOnClickListener {

        }
        binding.btnContinue.setOnClickListener {
            Toast.makeText(this, "prof", Toast.LENGTH_SHORT).show()
        }
        binding.cbTerms.setOnCheckedChangeListener { _, checkedTerms ->
            setButtonContinueState(checkedTerms, binding.cbProtectionPolicy.isChecked)
        }
        binding.cbProtectionPolicy.setOnCheckedChangeListener { _, checkedProtection ->
            setButtonContinueState(binding.cbTerms.isChecked, checkedProtection)
        }
    }

    //Function to init RegisterCreditActivity sending the necessary extras
    private fun startRegisterCreditActivity() {
        val intent = Intent(this, RegisterCreditActivity::class.java)
        intent.putExtra(EXT_AMOUNT_REQUESTED, valueMoney)
        intent.putExtra(EXT_DAYS, valueDays)
        startActivity(intent)
        finish()
    }


    //Function to set button continue state according to terms checked
    private fun setButtonContinueState(checkedTerms: Boolean, checkedProtection: Boolean) {
        if (checkedTerms && checkedProtection) {
            binding.btnContinue.isEnabled = true
            binding.btnContinue.setBackgroundColor(getColor(R.color.accent))

        } else {
            binding.btnContinue.isEnabled = false
            binding.btnContinue.setBackgroundColor(getColor(R.color.buttonDisable))
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startRegisterCreditActivity()
    }


}
