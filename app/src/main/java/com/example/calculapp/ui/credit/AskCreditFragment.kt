package com.example.calculapp.ui.credit

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.calculapp.R
import com.example.calculapp.data.preference.KeepLogin
import com.example.calculapp.data.preference.model.KeepLoginModel
import com.example.calculapp.data.sqlite.UserCreditsDataBase
import com.example.calculapp.databinding.FragmentAskCreditBinding
import com.example.calculapp.domain.credit.CalculateCredit
import com.example.calculapp.ui.register.UserRegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Currency

@AndroidEntryPoint
class AskCreditFragment : Fragment() {

    //ViewBinding
    private var _binding: FragmentAskCreditBinding? = null
    private val binding get() = _binding!!

    //Val and var
    private lateinit var moneyFormat: NumberFormat
    private val calculateCredit = CalculateCredit()
    private lateinit var keepLoginModel: KeepLoginModel

    //Database
    private lateinit var userCreditsDataBase: UserCreditsDataBase
    private lateinit var keepLogin: KeepLogin

    //Datastore

    //Constants
    companion object {
        //Constants to extras for next screen
        const val EXT_AMOUNT_REQUESTED = "extAmountRequest"
        const val EXT_DAYS = "extDays"
    }


    //Function when the view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }


    //Function to init and configure user interface
    private fun initUi() {
        initLocalStorage()
        initFormats()
        initListeners()
        setFirstValues()
        setTooltipsText()
    }


    //Function to init database and preferences settings
    private fun initLocalStorage() {
        userCreditsDataBase = UserCreditsDataBase(requireContext())
        keepLogin = KeepLogin(requireContext())

        CoroutineScope(Dispatchers.IO).launch {
            keepLogin.getAutoLogin().collect { keepLoginModelData ->
                keepLoginModel = keepLoginModelData
            }
        }
    }


    //Function to sliders format
    private fun initFormats() {
        moneyFormat = NumberFormat.getCurrencyInstance()
        moneyFormat.maximumFractionDigits = 0
        moneyFormat.currency = Currency.getInstance("USD")

        binding.quantitySlider.setLabelFormatter { value -> moneyFormat.format(value.toDouble()) }
        binding.daysSlider.setLabelFormatter { value -> "${value.toInt()} Días" }
    }


    //Function to init click listeners
    private fun initListeners() {
        binding.quantitySlider.addOnChangeListener { _, valueMoney, _ ->
            setCreditData(valueMoney.toInt(), binding.daysSlider.value.toInt())
        }
        binding.daysSlider.addOnChangeListener { _, valueDays, _ ->
            setCreditData(binding.quantitySlider.value.toInt(), valueDays.toInt())
            setStepSizeDaysSlider(valueDays)
        }
        binding.btnRequestsCredit.setOnClickListener {
            if (keepLoginModel.logging) {
                val messageResponse = userCreditsDataBase.insertCredit(
                    binding.quantitySlider.value.toInt(),
                    binding.daysSlider.value.toInt(),
                    keepLoginModel.userIdentificationNumber
                    )

                Toast.makeText(requireContext(), messageResponse.message, Toast.LENGTH_SHORT).show()
            } else {
                startUserRegisterActivity()
            }
        }
    }

    //Function to set credit data according sliders selection
    private fun setCreditData(valueMoney: Int, valueDays: Int) {
        val calculatedCreditModel = calculateCredit.calculateDataCredit(valueMoney, valueDays)

        binding.tvTotalPay.text = moneyFormat.format(calculatedCreditModel.total)
        binding.tvRequestedAmount.text = moneyFormat.format(calculatedCreditModel.amountRequested)
        binding.tvInterest.text = moneyFormat.format(calculatedCreditModel.interest)
        binding.tvGuarantee.text = moneyFormat.format(calculatedCreditModel.endorsement)
        binding.tvGuaranteeDiscount.text = getString(
            R.string.negativeNumber,
            moneyFormat.format(calculatedCreditModel.endorsementDiscount)
        )
        binding.tvElectronicSignature.text = moneyFormat.format(calculatedCreditModel.electronicSignature)
        binding.tvDiscount.text = getString(R.string.negativeNumber, moneyFormat.format(calculatedCreditModel.discount))
        binding.tvTotal.text = moneyFormat.format(calculatedCreditModel.total)
    }

    //Function to set step size for daysSlider according to value
    private fun setStepSizeDaysSlider(valueDays: Float) {
        if (valueDays.toInt() >= 31 && binding.daysSlider.valueFrom == 5f) {
            binding.daysSlider.value = 30f
            binding.daysSlider.valueFrom = 0f
            binding.daysSlider.stepSize = 30f

        } else if (valueDays.toInt() == 0) {
            binding.daysSlider.stepSize = 1f
            binding.daysSlider.valueFrom = 5f
            binding.daysSlider.value = 30f
        }
    }

    //Function to init UserRegisterActivity sending the necessary extras
    private fun startUserRegisterActivity() {
        val intent = Intent(requireContext(), UserRegisterActivity::class.java)
        intent.putExtra(EXT_AMOUNT_REQUESTED, binding.quantitySlider.value.toInt())
        intent.putExtra(EXT_DAYS, binding.daysSlider.value.toInt())
        startActivity(intent)
        activity?.finish()
    }


    //Function to init sliders first values
    private fun setFirstValues() {
        val firstMoneyValue =
            activity?.intent?.extras?.getInt(EXT_AMOUNT_REQUESTED)?.toFloat() ?: 250000f
        binding.quantitySlider.value = firstMoneyValue

        val firstDayValue = activity?.intent?.extras?.getInt(EXT_DAYS)?.toFloat() ?: 10f
        if (firstMoneyValue > 30) {
            setStepSizeDaysSlider(firstDayValue)
        }
        binding.daysSlider.value = firstDayValue
    }


    //Function to init tooltips text for info
    private fun setTooltipsText() {
        binding.btnDaysInfo.tooltipText = getString(R.string.questionWhenPayTooltip)
        binding.btnInterest.tooltipText = getString(R.string.interestTooltip)
        binding.btnGuarantee.tooltipText = getString(R.string.guaranteeTooltip)
        binding.btnGuaranteeDiscount.tooltipText = getString(R.string.guaranteeDiscountTooltip)
        binding.btnElectronicSignature.tooltipText = getString(R.string.electronicSignatureTooltip)
        binding.btnDiscount.tooltipText = getString(R.string.discountTooltip)
    }


    //Inflate view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAskCreditBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


}