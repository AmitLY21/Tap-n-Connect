package com.amitdev.tap_n_connect.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.amitdev.tap_n_connect.QRGenerator
import com.amitdev.tap_n_connect.R
import com.amitdev.tap_n_connect.databinding.FragmentViewCardsBinding
import com.google.gson.Gson

class MyCardRecyclerViewAdapter(
    private val cardList: List<Card>
) : RecyclerView.Adapter<MyCardRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentViewCardsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = cardList[position]
        holder.textName.text = item.name
        holder.textJobTitle.text = item.jobTitle
        holder.textCompany.text = item.company
        holder.textEmail.text = item.email
        holder.textPhoneNumber.text = item.phoneNumber
        if (item.address.isNotEmpty()) {
            holder.textAddress.text = item.address
        } else {
            holder.textAddress.visibility = View.GONE
        }
        if (item.website.isNotEmpty()) {
            holder.textWebsite.text = item.website
        } else {
            holder.textWebsite.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            showBottomSheetDialog(it.context, item)
        }

        Log.d("Tappy", "onBindViewHolder: $item")
    }

    fun showBottomSheetDialog(context: Context, item: Card) {
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.bottom_sheet, null)
        dialogBuilder.setView(dialogView)
        val cardHeader = dialogView.findViewById<TextView>(R.id.card_name)
        cardHeader.text = item.name
        val addContact = dialogView.findViewById<TextView>(R.id.upload_to_contacts)
        addContact.setOnClickListener {
            addContact(context, item)
            Toast.makeText(context, "Add Contact clicked", Toast.LENGTH_SHORT).show()
        }
        val chatWhatsApp = dialogView.findViewById<TextView>(R.id.send_whatsapp)
        chatWhatsApp.setOnClickListener {
            val phoneNumber = item.phoneNumber
            startWhatsappChat(context, phoneNumber)
        }
        val addLinkedIn = dialogView.findViewById<TextView>(R.id.open_link)
        addLinkedIn.setOnClickListener {
            if (item.website.isNotEmpty()) {
                openWebsite(it.context, item.website)
                Toast.makeText(context, "Opened Website", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Does not have a website link!", Toast.LENGTH_SHORT).show()

            }
        }
        val shareCard = dialogView.findViewById<TextView>(R.id.share_card)
        shareCard.setOnClickListener {
            val gson = Gson()
            QRGenerator.generateQR(it.context,gson.toJson(item,Card::class.java))
        }

        val removeCard = dialogView.findViewById<TextView>(R.id.remove_card)
        removeCard.setOnClickListener {
            val sharedPreferencesHelper = SharedPreferencesHelper(it.context)
            val index = sharedPreferencesHelper.removeCard(item)
            notifyItemRemoved(index)
            Toast.makeText(context, "Card removed. Please refresh the view to see the changes.", Toast.LENGTH_SHORT).show()
        }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    fun openWebsite(context: Context, websiteLink: String) {
        val openWebsiteIntent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteLink))
        context.startActivity(openWebsiteIntent)
    }


    @Suppress("DEPRECATION")
    private fun startWhatsappChat(context: Context, phoneNumber: String) {
        val packageManager = context.packageManager
        val isWhatsAppInstalled =
            packageManager.getInstalledApplications(PackageManager.GET_META_DATA).any {
                it.packageName == "com.whatsapp"
            }
        if (isWhatsAppInstalled) {
            val sendIntent = Intent("android.intent.action.MAIN")
            sendIntent.putExtra("jid", phoneNumber + "@s.whatsapp.net")
            sendIntent.setPackage("com.whatsapp")
            context.startActivity(sendIntent)
        } else {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://web.whatsapp.com/send?phone=$phoneNumber")
            )
            context.startActivity(browserIntent)
        }
    }

    fun addContact(context: Context, item: Card) {
        val intent = Intent(ContactsContract.Intents.Insert.ACTION)
        intent.type = ContactsContract.RawContacts.CONTENT_TYPE
        intent.putExtra(ContactsContract.Intents.Insert.NAME, item.name)
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, item.phoneNumber)
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, item.email)
        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, item.company)
        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, item.jobTitle)
        context.startActivity(intent)
    }

    override fun getItemCount(): Int = cardList.size

    inner class ViewHolder(binding: FragmentViewCardsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var textName: TextView = binding.root.findViewById(R.id.text_name)
        var textJobTitle: TextView = binding.root.findViewById(R.id.text_job_title)
        var textCompany: TextView = binding.root.findViewById(R.id.text_company)
        var textEmail: TextView = binding.root.findViewById(R.id.text_email)
        var textPhoneNumber: TextView = binding.root.findViewById(R.id.text_phone_number)
        var textAddress: TextView = binding.root.findViewById(R.id.text_address)
        var textWebsite: TextView = binding.root.findViewById(R.id.text_website)


        override fun toString(): String {
            return "ViewHolder(textName=$textName, textJobTitle=$textJobTitle, textCompany=$textCompany, textEmail=$textEmail, textPhoneNumber=$textPhoneNumber, textAddress=$textAddress, textWebsite=$textWebsite)"
        }


    }

}