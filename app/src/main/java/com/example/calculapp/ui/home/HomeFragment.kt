package com.example.calculapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calculapp.R
import com.example.calculapp.data.sqlite.UserCreditsDataBase
import com.example.calculapp.data.sqlite.model.CreditModel
import com.example.calculapp.databinding.FragmentHomeBinding
import com.example.calculapp.domain.credit.CreditState
import com.example.calculapp.ui.home.adapter.HomeAdapter
import com.example.calculapp.ui.home.adapter.HomeListeners
import com.example.calculapp.ui.main.MainHomeActivity.Companion.USER_IDENTIFICATION_NUMBER
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.Currency

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeListeners {

    //ViewBinding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    //Database
    private lateinit var userCreditsDataBase: UserCreditsDataBase

    //RecyclerView
    private var creditModelList: List<CreditModel>? = null

    //Val and var
    private lateinit var moneyFormat: NumberFormat
    private var userIdentificationNumber: Long? = null


    //Function when the view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }


    //Function to init and configure user interface
    private fun initUi() {
        initDatabase()
        initExtras()
        initFormats()
        if (userIdentificationNumber != null) {
            initRecyclerView()
            initUserInfo()
        }
    }


    //Function to init database
    private fun initDatabase() {
        userCreditsDataBase = UserCreditsDataBase(requireContext())
    }


    //Function to recover extras
    private fun initExtras() {
        userIdentificationNumber = activity?.intent?.extras?.getLong(USER_IDENTIFICATION_NUMBER)
    }


    //Function to init money format
    private fun initFormats() {
        moneyFormat = NumberFormat.getCurrencyInstance()
        moneyFormat.maximumFractionDigits = 0
        moneyFormat.currency = Currency.getInstance("USD")
    }


    //Function to recover data and set information in recyclerview
    private fun initRecyclerView() {
        creditModelList = userCreditsDataBase.allUserCredit(userIdentificationNumber!!)
        if (creditModelList != null) {
            binding.rvDebtHistory.layoutManager = LinearLayoutManager(requireContext())
            val homeAdapter = HomeAdapter(creditModelList!!, this)
            binding.rvDebtHistory.adapter = homeAdapter

            currentDebt()

        } else {
            Toast.makeText(requireContext(), "Ha ocurrido un error con los creditos intentelo mas tarde", Toast.LENGTH_SHORT).show()
        }
    }

    //Function to set current debt
    private fun currentDebt() {
        binding.tvBadge.text = getString(R.string.badgeCop)

        var creditModelCurrent: CreditModel? = null
        creditModelList?.forEach { creditModel ->
            if (creditModel.state == CreditState.Actual.toString()) {
                creditModelCurrent = creditModel
            }
        }
        binding.tvTotalDebt.text = if (creditModelCurrent != null) creditModelCurrent!!.total.toString() else "0.0"
    }


    //Function to ini and set user information
    private fun initUserInfo() {
        val userModel = userCreditsDataBase.getUserInformation(userIdentificationNumber!!)
        if (userModel != null) {
            binding.tvUserName.text = userModel.name

        } else {
            Toast.makeText(requireContext(), "Ha ocurrido un error intentelo mas tarde", Toast.LENGTH_SHORT).show()
        }
    }


    //Function lambda to click on the item detail button
    override fun onButtonDetailCLick(creditModel: CreditModel) {

    }


    //Inflate view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


}