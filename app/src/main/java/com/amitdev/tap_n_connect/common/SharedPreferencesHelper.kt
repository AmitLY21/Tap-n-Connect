package com.amitdev.tap_n_connect.common

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesHelper(context: Context) {
    private val PREFS_FILENAME = "com.amitdev.prefs"
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    fun saveCards(cards: List<Card>) {
        val editor = prefs.edit()
        val distinctCards = cards.distinctBy { it.phoneNumber }
        val json = Gson().toJson(distinctCards)
        editor.putString("cards", json)
        editor.apply()
    }

    fun updateCardsList(newCard: Card) {
        val cards = getCards().toMutableList()
        if (!cards.contains(newCard)) {
            cards.add(newCard)
            saveCards(cards)
            Log.d("Tappy", "updateCardsList: $newCard")
        }
        Log.d("Tappy", "AllCards: ${getCards()}")

    }

    fun removeCard(card: Card) : Int {
        val cards = getCards().toMutableList()
        val index = cards.indexOf(card)
        val updatedCards = cards.filter { it.id != card.id }
        saveCards(updatedCards)
        Log.d("Tappy", "Removed Card: $card")
        Log.d("Tappy", "AllCards: ${getCards()}")
        return index
    }


    fun getCards(): List<Card> {
        val json = prefs.getString("cards", null)
        return if (json != null) {
            val type = object : TypeToken<List<Card>>() {}.type
            Gson().fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun saveCard(card: Card) {
        val editor = prefs.edit()
        val json = Gson().toJson(card)
        editor.putString("card", json)
        editor.apply()
    }

    fun getCard(): Card? {
        val json = prefs.getString("card", null)
        return if (json != null) {
            Gson().fromJson(json, Card::class.java)
        } else {
            null
        }
    }
}
