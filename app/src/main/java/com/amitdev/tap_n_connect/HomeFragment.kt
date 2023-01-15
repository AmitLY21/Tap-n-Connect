package com.amitdev.tap_n_connect

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnShareCard = view.findViewById<MaterialButton>(R.id.btnShare)
        val sharedPreferencesHelper: SharedPreferencesHelper? = SharedPreferencesHelper(requireContext())
        btnShareCard.setOnClickListener {
            showAndTransferCardDialog(sharedPreferencesHelper)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {

            }
    }

    @SuppressLint("SetTextI18n")
    private fun showAndTransferCardDialog(sharedPreferencesHelper: SharedPreferencesHelper?) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_card, null)
        builder.setView(dialogView)
        val card = getUserCardFromSP(sharedPreferencesHelper)
        // Find the TextViews in the dialog layout
        val textName = dialogView.findViewById<TextView>(R.id.text_name)
        val textJobTitle = dialogView.findViewById<TextView>(R.id.text_job_title)
        val textCompany = dialogView.findViewById<TextView>(R.id.text_company)
        val textEmail = dialogView.findViewById<TextView>(R.id.text_email)
        val textPhoneNumber = dialogView.findViewById<TextView>(R.id.text_phone_number)

        // Set the text of the TextViews to the card data
        if (card.name.length != 0) {
            textName.text = "Name: ${card.name}"
            textJobTitle.text = "Job Title: ${card.jobTitle}"
            textCompany.text = "Company: ${card.company}"
            textEmail.text = "Email: ${card.email}"
            textPhoneNumber.text = "Phone Number: ${card.phoneNumber}"

            builder.setTitle("Card Info")
                .setPositiveButton("SHARE") { _, _ ->
                    transferCardDetailsViaNFC(card)
                }
            val dialog = builder.create()
            dialog.show()
        } else {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Create your business card!")
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog = builder.create()
            alertDialog.show()

        }
    }





    private fun transferCardDetailsViaNFC(card: Card) {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(requireContext())
        if (nfcAdapter != null && nfcAdapter.isEnabled) {
            val cardJson = Gson().toJson(card) // Convert the card object to JSON
            val ndefMessage =
                NdefMessage(NdefRecord.createMime("application/json", cardJson.toByteArray()))

            // Set the NDEF message to the NFC adapter
            nfcAdapter.setNdefPushMessage(ndefMessage, requireActivity())
            Toast.makeText(
                requireContext(),
                "Card details transferred via NFC.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(requireContext(), "NFC is not enabled. Redirects to Settings", Toast.LENGTH_SHORT).show()
            Thread.sleep(1500)
            val intent = Intent(Settings.ACTION_NFC_SETTINGS)
            val REQUEST_CODE_NFC = 0
            startActivityForResult(intent, REQUEST_CODE_NFC)
        }
    }


    private fun getUserCardFromSP(sharedPreferencesHelper: SharedPreferencesHelper?): Card {
        val card = sharedPreferencesHelper?.getCard()
        if (card != null)
            return card
        else
            return Card(
                name = "",
                jobTitle = "",
                phoneNumber = "",
                email = "",
                company = "",
                address = "",
                website = ""
            )
    }

}
