package com.amitdev.tap_n_connect.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.amitdev.tap_n_connect.QRGenerator
import com.amitdev.tap_n_connect.R
import com.amitdev.tap_n_connect.common.Card
import com.google.android.material.button.MaterialButton


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ScanCardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScanCardFragment : DialogFragment() {
    lateinit var lottieScanQR: LottieAnimationView
    lateinit var btnScan: MaterialButton
    var lbltitle: TextView? = null
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
        lottieScanQR = view.findViewById<LottieAnimationView>(R.id.qrscan)
        btnScan = view.findViewById(R.id.btnScan)
        lbltitle = view.findViewById(R.id.lblScannerTitle)

        btnScan.setOnClickListener {
            Log.d(TAG, "onViewCreated: Scanning")
            QRGenerator.readQRCode(requireContext())
        }
    }

    companion object {
        val TAG: String = ScanCardFragment::class.java.simpleName
        fun newInstance(): ScanCardFragment {
            return ScanCardFragment()
        }
    }
}