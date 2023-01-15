package com.amitdev.tap_n_connect

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.google.gson.Gson

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ScanCardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScanCardFragment : Fragment() {
    var lottieRadar: LottieAnimationView? = null
    var lottieNfcDown: LottieAnimationView? = null
    var lbltitle: TextView? = null
    val nfcAdapter: NfcAdapter? = null
    val card: Card? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_scan_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lottieRadar = view.findViewById<LottieAnimationView>(R.id.radar)
        lottieNfcDown = view.findViewById<LottieAnimationView>(R.id.nfc_down)
        lbltitle = view.findViewById(R.id.lblScannerTitle)
        val nfcAdapter = NfcAdapter.getDefaultAdapter(requireContext())
        receiveCardDetailsViaNFC()

        nfcAdapter?.setOnNdefPushCompleteCallback(object : NfcAdapter.OnNdefPushCompleteCallback {
            override fun onNdefPushComplete(event: NfcEvent?) {
                Toast.makeText(
                    requireContext(),
                    "Card details received via NFC.",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }, requireActivity())

    }

    private fun receiveCardDetailsViaNFC() {
        if (nfcAdapter != null && nfcAdapter.isEnabled) {
            nfcAdapter.setNdefPushMessageCallback(object : NfcAdapter.CreateNdefMessageCallback {
                override fun createNdefMessage(event: NfcEvent?): NdefMessage {
                    val intent = Intent(requireActivity(), javaClass)
                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                    val pendingIntent = PendingIntent.getActivity(requireActivity(), 0, intent, 0)
                    val ndefRecord =
                        NdefRecord.createMime("application/json", Gson().toJson(card).toByteArray())
                    return NdefMessage(
                        ndefRecord,
                        NdefRecord.createApplicationRecord("com.amitdev.tap_n_connect"),
                        NdefRecord.createApplicationRecord("com.amitdev.tap_n_connect")
                    )
                }
            }, requireActivity())
        } else {
            Toast.makeText(requireContext(), "NFC is not enabled.", Toast.LENGTH_SHORT).show()
            lottieRadar?.pauseAnimation()
            lottieRadar?.visibility = View.GONE
            lottieNfcDown?.visibility = View.VISIBLE
            lbltitle?.setText("NFC is disabled")

        }
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScanCardFragment().apply {

            }
    }
}