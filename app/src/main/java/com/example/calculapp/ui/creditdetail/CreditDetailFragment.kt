package com.example.calculapp.ui.creditdetail

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.example.calculapp.R
import com.example.calculapp.data.sqlite.UserCreditsDataBase
import com.example.calculapp.data.sqlite.model.CreditModel
import com.example.calculapp.databinding.FragmentCreditDetailBinding
import com.example.calculapp.domain.credit.CalculateCredit
import com.example.calculapp.domain.credit.CreditState
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.Currency

@AndroidEntryPoint
class CreditDetailFragment : DialogFragment() {

    //ViewBinding
    private var _binding: FragmentCreditDetailBinding? = null
    private val binding get() = _binding!!

    //Safe arguments
    private val args by navArgs<CreditDetailFragmentArgs>()

    //DataBase
    private lateinit var userCreditsDataBase: UserCreditsDataBase

    //Val and var
    private var creditModel: CreditModel? = null
    private lateinit var moneyFormat: NumberFormat
    private lateinit var dialog: Dialog


    //Function when the view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }


    //Function to init and configure user interface
    private fun initUi() {
        initDataBase()
        initFormats()
        if (creditModel != null) {
            initCreditData()
        } else {
            Toast.makeText(requireContext(), "Ha ocurrido un error intentelo mas tarde", Toast.LENGTH_SHORT).show()
        }
        initListeners()
    }


    //Function to init data base
    private fun initDataBase() {
        userCreditsDataBase = UserCreditsDataBase(requireContext())
        creditModel = userCreditsDataBase.getCreditID(args.creditID)
    }


    //Function to init text formats
    private fun initFormats() {
        moneyFormat = NumberFormat.getCurrencyInstance()
        moneyFormat.maximumFractionDigits = 0
        moneyFormat.currency = Currency.getInstance("USD")
    }


    //Function to init and set credit data
    private fun initCreditData() {
        val calculateCredit = CalculateCredit()
        val calculateCreditModel = calculateCredit.calculateDataCredit(creditModel!!.amountRequested, creditModel!!.daysRequested)

        binding.tvAmountRequested.text = moneyFormat.format(creditModel!!.amountRequested)
        binding.tvCreditTime.text = getString(R.string.daysString, creditModel!!.daysRequested)
        binding.tvApplicationDate.text = creditModel!!.creditDate
        binding.tvInterest.text = moneyFormat.format(calculateCreditModel.interest)
        binding.tvEndorsement.text = moneyFormat.format(calculateCreditModel.endorsement)
        binding.tvEndorsementDiscount.text = getString(R.string.negativeNumber, moneyFormat.format(calculateCreditModel.endorsementDiscount))
        binding.tvElectronicSignature.text = moneyFormat.format(calculateCreditModel.electronicSignature)
        binding.tvDiscount.text = getString(R.string.negativeNumber, moneyFormat.format(calculateCreditModel.discount))
        binding.tvTotalPay.text = moneyFormat.format(creditModel!!.total)

        setButtonsVisibility(creditModel!!.state)
    }

    //Function to change buttons visibility according credit state
    private fun setButtonsVisibility(creditState: String) {
        when(creditState) {
            CreditState.Actual.toString() -> binding.btnPayTheCredit.visibility = View.VISIBLE
            CreditState.Proceso.toString() -> binding.llButtons.visibility = View.VISIBLE
        }
    }


    //Function to init click listeners
    private fun initListeners() {
        binding.btnClose.setOnClickListener { dialog.dismiss() }
        binding.btnAcceptCredit.setOnClickListener {
            creditModel!!.state = CreditState.Actual.toString()
            if (userCreditsDataBase.updateStateCredit(creditModel!!) > 0) {
                Toast.makeText(requireContext(), "Credito aceptado", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
        binding.btnCancelCredit.setOnClickListener {
            if (userCreditsDataBase.deleteCredit(args.creditID) > 0) {
                Toast.makeText(requireContext(), "Credito cancelado", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
        binding.btnPayTheCredit.setOnClickListener {
            creditModel!!.state = CreditState.Finalizado.toString()
            if (userCreditsDataBase.updateStateCredit(creditModel!!) > 0) {
                Toast.makeText(requireContext(), "Credito pagado", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
    }


    // Inflate the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreditDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    //Function to initialize dialog window as transparent
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }


}