package com.example.calculapp.ui.home.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.calculapp.R
import com.example.calculapp.data.sqlite.model.CreditModel
import com.example.calculapp.databinding.ItemCreditBinding
import java.text.NumberFormat
import java.util.Currency

class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    //ViewBinding
    val binding = ItemCreditBinding.bind(view)

    //Val and var
    private lateinit var moneyFormat: NumberFormat


    //Function to init money format
    private fun initFormat() {
        moneyFormat = NumberFormat.getCurrencyInstance()
        moneyFormat.maximumFractionDigits = 0
        moneyFormat.currency = Currency.getInstance("USD")
    }


    //Function to set credit data in the item
    fun render(creditModel: CreditModel) {
        initFormat()
        binding.tvCreditDate.text = creditModel.creditDate
        binding.tvBadge.text = binding.tvBadge.context.getString(R.string.badgeCop)
        binding.tvTotalCredit.text = moneyFormat.format(creditModel.total)
        binding.tvCreditState.text = creditModel.state
    }

}