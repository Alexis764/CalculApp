package com.example.calculapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.calculapp.R
import com.example.calculapp.databinding.FragmentSettingsBinding
import com.example.calculapp.domain.model.BadgesModel
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Currency

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    //ViewBinding
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private val settingsViewModel by viewModels<SettingsViewModel>()

    //Constants
    companion object {
        //Retrofit API BADGES
        const val API_KEY = "3b6037a9f522ffb9a235667946e89e5a"
    }

    //Val and var
    private lateinit var badgesModel: BadgesModel


    //Function when the view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        settingsViewModel.getBadge(API_KEY, "USD,COP,MXN")
    }


    //Function to init and configure user interface
    private fun initUi() {
        initBadgeOptions()
        initListeners()
        initUiState()
    }


    //Function to init all badges options
    private fun initBadgeOptions() {
        val badgeArray = arrayOf(
            getString(R.string.badgeCop),
            getString(R.string.badgeUsd),
            getString(R.string.badgeMxm)
        )
        binding.actvSelectCurrency.setText(getString(R.string.badgeCop))
        (binding.tilSelectCurrency.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(
            badgeArray
        )
    }


    //Function to init click listeners
    private fun initListeners() {
        binding.tiedEUR.addTextChangedListener { calculateMoneyConverter() }
        binding.actvSelectCurrency.addTextChangedListener { calculateMoneyConverter() }
    }

    //Function to calculate and set money
    private fun calculateMoneyConverter() {
        val moneyEur = binding.tiedEUR.text.toString()

        if (moneyEur.isNotEmpty()) {
            val moneyConverter = when (binding.actvSelectCurrency.text.toString()) {
                getString(R.string.badgeCop) -> (moneyEur.toLong() * badgesModel.cop)
                getString(R.string.badgeUsd) -> (moneyEur.toLong() * badgesModel.usd)
                getString(R.string.badgeMxm) -> (moneyEur.toLong() * badgesModel.mxn)
                else -> { 1.0 }
            }

            val money = NumberFormat.getCurrencyInstance()
            money.currency = Currency.getInstance("USD")
            binding.tiedBadge.setText(money.format(moneyConverter))
        }
    }


    //Function to init settings state
    private fun initUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingsViewModel.settingsState.collect { settingsState ->
                    when (settingsState) {
                        SettingsState.Loading -> loadingState()
                        is SettingsState.Error -> errorState(settingsState)
                        is SettingsState.Success -> successState(settingsState)
                    }
                }
            }
        }
    }

    //Function when ui loading state
    private fun loadingState() {
        binding.progress.visibility = View.VISIBLE
    }

    //Function when ui error state
    private fun errorState(settingsState: SettingsState.Error) {
        binding.progress.visibility = View.GONE
        Toast.makeText(requireContext(), settingsState.error, Toast.LENGTH_SHORT).show()
    }

    //Function when ui success state
    private fun successState(settingsState: SettingsState.Success) {
        binding.progress.visibility = View.GONE
        binding.llCalculator.visibility = View.VISIBLE
        badgesModel = settingsState.badgeModel.badges
    }


    //Inflate view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


}