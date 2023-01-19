package com.amitdev.tap_n_connect

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.amitdev.tap_n_connect.common.Card
import com.amitdev.tap_n_connect.common.SharedPreferencesHelper
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class QRGenerator {
    companion object {
        fun generateQR(context: Context, inputString: String) {
            val multiFormatWriter = MultiFormatWriter()
            try {
                val bitMatrix =
                    multiFormatWriter.encode(inputString, BarcodeFormat.QR_CODE, 1000, 1000)
                val barcodeEncoder = BarcodeEncoder()
                val bitmap = barcodeEncoder.createBitmap(bitMatrix)
                val imageView = ImageView(context)
                imageView.setImageBitmap(bitmap)
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Card QR Code")
                builder.setView(imageView)
                builder.setView(imageView)
                builder.setPositiveButton("Close") { dialog, _ -> dialog.dismiss() }
                val dialog = builder.create()
                dialog.setCanceledOnTouchOutside(false)
                dialog.show()
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }

        fun readQRCode(context: Context) {
            val barcodeDetector =
                BarcodeDetector.Builder(context).setBarcodeFormats(Barcode.QR_CODE).build()
            if (!barcodeDetector.isOperational) {
                Toast.makeText(context, "Could not set up the detector!", Toast.LENGTH_SHORT).show()
                return
            }
            val cameraSource = CameraSource.Builder(context, barcodeDetector)
                .setAutoFocusEnabled(true)
                .build()
            val cameraPreview = SurfaceView(context)
            cameraPreview.holder.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceChanged(
                    holder: SurfaceHolder,
                    format: Int,
                    width: Int,
                    height: Int
                ) {
                }

                override fun surfaceDestroyed(holder: SurfaceHolder) {
                    cameraSource.stop()
                }

                override fun surfaceCreated(holder: SurfaceHolder) {
                    try {
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            return
                        }
                        cameraSource.start(holder)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            })
            val builder = AlertDialog.Builder(context)
            builder.setView(cameraPreview)
            builder.setPositiveButton("Close") { dialog, _ ->
                cameraSource.stop()
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
            var qrCodeDetected = false
            barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
                override fun release() {}
                override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                    val sharedPreferencesHelper = SharedPreferencesHelper(context)
                    if (!qrCodeDetected) {
                        val barcodes = detections.detectedItems
                        if (barcodes.size() != 0) {
                            try {
                                val barcode = barcodes.valueAt(0)
                                val jsonString = barcode.displayValue
                                val jsonObject = JSONObject(jsonString)
                                if (jsonObject.has("className")) {
                                    val className = jsonObject.getString("className")
                                    if (className == "Card") {
                                        val gson = Gson()
                                        val card = gson.fromJson(jsonString, Card::class.java)
                                        Log.d("Tappy", "receiveDetections: $card")
                                        val cards = ArrayList<Card>()
                                        cards.add(card)
                                        if (cards.size == 1) {
                                            sharedPreferencesHelper.updateCardsList(card)
                                            qrCodeDetected = true
                                            Handler(Looper.getMainLooper()).post {
                                                cameraSource.release()
                                                dialog.dismiss()
                                            }
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Invalid QR Code",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Invalid QR Code, missing className field",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                Handler(Looper.getMainLooper()).postDelayed({
                                    Toast.makeText(
                                        context,
                                        "Valid QR Code , Added to you Card List!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }, 1500)


                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
        }


/*
        fun readQR(activity : Activity){
            val integrator = IntentIntegrator(activity)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            integrator.setPrompt("scan")
            integrator.setCameraId(0)
            integrator.setBeepEnabled(false)
            integrator.setBarcodeImageEnabled(false)
            integrator.initiateScan()
        }

        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            val scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            scanResult.let {
                Log.d("Tappy", "onActivityResult: ${scanResult.contents}")
            }
        }


        fun readQRCode(context: Context): Card? {
            val scanner = IntentIntegrator.forSupportFragment(this)
            scanner.setBeepEnabled(false)
            scanner.initiateScan()
            val scanResult = scanner.parseActivityResult(requestCode, resultCode, data)
            if (scanResult != null) {
                val contents = scanResult.contents
                try {
                    val jsonObject = JSONObject(contents)
                    val className = jsonObject.getString("class")
                    if (className == "Card") {
                        val card = Gson().fromJson(jsonObject.toString(), Card::class.java)
                        return card
                    } else {
                        Toast.makeText(context, "Invalid QR Code", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            return null
        }
*/
        /*
        fun readQR(context: Context) {
            val scanner = IntentIntegrator(context)
            scanner.initiateScan()
            val result = scanner.parseActivityResult(Activity.RESULT_OK, Intent())
            val jsonString = result.contents
            return jsonString
        }*/

    }
}
