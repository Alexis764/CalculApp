package com.example.calculapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calculapp.R
import com.example.calculapp.data.sqlite.model.CreditModel

class HomeAdapter(
    private val creditModelList: List<CreditModel>,
    private val homeListeners: HomeListeners
): RecyclerView.Adapter<HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_credit, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.render(creditModelList[position])

        holder.binding.btnDetail.setOnClickListener {
            homeListeners.onButtonDetailCLick(creditModelList[position])
        }
    }

    override fun getItemCount() = creditModelList.size

}