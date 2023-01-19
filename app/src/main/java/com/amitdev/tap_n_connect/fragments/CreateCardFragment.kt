package com.amitdev.tap_n_connect.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.amitdev.tap_n_connect.common.Card
import com.amitdev.tap_n_connect.R
import com.amitdev.tap_n_connect.common.SharedPreferencesHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class CreateCardFragment : Fragment() {
    var sharedPreferencesHelper : SharedPreferencesHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())
        val btnAddCard : MaterialButton = view.findViewById(R.id.btnAddCard)
        btnAddCard.setOnClickListener {
            var card: Card?
            val currentCard = fetchTextFromTextInputLayouts(view)
            card = currentCard
            Log.d("Tappy", "onViewCreated: ${currentCard.toString()}")
            sharedPreferencesHelper!!.saveCard(card)
            clearTextInputLayouts(view)
        }
    }

    fun clearTextInputLayouts(view: View) {
        view.findViewById<TextInputLayout>(R.id.name).editText?.setText("")
        view.findViewById<TextInputLayout>(R.id.jobTitle).editText?.setText("")
        view.findViewById<TextInputLayout>(R.id.phoneNumber).editText?.setText("")
        view.findViewById<TextInputLayout>(R.id.email).editText?.setText("")
        view.findViewById<TextInputLayout>(R.id.company).editText?.setText("")
        view.findViewById<TextInputLayout>(R.id.address).editText?.setText("")
        view.findViewById<TextInputLayout>(R.id.website).editText?.setText("")
    }


    fun fetchTextFromTextInputLayouts(view: View) : Card {
        val name = view.findViewById<TextInputLayout>(R.id.name).editText?.text.toString()
        val jobTitle = view.findViewById<TextInputLayout>(R.id.jobTitle).editText?.text.toString()
        val phoneNumber = view.findViewById<TextInputLayout>(R.id.phoneNumber).editText?.text.toString()
        val email = view.findViewById<TextInputLayout>(R.id.email).editText?.text.toString()
        val company = view.findViewById<TextInputLayout>(R.id.company).editText?.text.toString()
        val address = view.findViewById<TextInputLayout>(R.id.address).editText?.text.toString()
        val website = view.findViewById<TextInputLayout>(R.id.website).editText?.text.toString()
        return Card(name, jobTitle, phoneNumber, email, company, address, website)
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateCardFragment().apply {

            }
    }
}