package com.amitdev.tap_n_connect.common

import java.io.Serializable
import java.util.*


class Card(val name: String, val jobTitle: String, val phoneNumber: String, val email: String, val company:String, val address:String, val website:String) :
    Serializable {

    val id = UUID.randomUUID().toString()
    val className = "Card"

    override fun toString(): String {
        return "Card(name='$name', jobTitle='$jobTitle', phoneNumber='$phoneNumber', email='$email', company='$company', address='$address', website='$website')"
    }

}
