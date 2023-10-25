package com.example.calculapp.ui.closeaccount

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.calculapp.data.preference.KeepLogin
import com.example.calculapp.databinding.FragmentCloseDialogBinding
import com.example.calculapp.ui.about.AboutActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CloseDialogFragment : DialogFragment() {

    //ViewBinding
    private var _binding: FragmentCloseDialogBinding? = null
    private val binding get() = _binding!!

    //val and var
    private lateinit var dialog: Dialog

    //Datastore
    private lateinit var keepLogin: KeepLogin


    //Function when the view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }


    //Function to init and configure user interface
    private fun initUi() {
        initDatastore()
        initListeners()
    }


    //Function to init datastore for keepLogin
    private fun initDatastore() {
        keepLogin = KeepLogin(requireContext())
    }


    //Function to init click listeners
    private fun initListeners() {
        binding.btnCancelLeave.setOnClickListener {
            dialog.dismiss()
        }
        binding.btnLeave.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                keepLogin.saveUserIdentificationNumber(-1)
                keepLogin.saveAutoLogin(false)
            }
            dialog.dismiss()
            val intent = Intent(requireContext(), AboutActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }


    // Inflate the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCloseDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    //Function to initialize dialog window as transparent
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }


}