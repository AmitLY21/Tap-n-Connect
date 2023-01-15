package com.amitdev.tap_n_connect

import android.Manifest.permission.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list = listOf<String>(NFC, INTERNET, WRITE_CONTACTS)
        managePermissions = ManagePermissions(this, list, PermissionsRequestCode)
        managePermissions.checkPermissions()

        val bottomNavBar = findViewById<BottomNavigationView>(R.id.bottom_nav_bar)

        bottomNavBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_menu_item -> {
                    // Show the home fragment
                    val homeFragment = HomeFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, homeFragment)
                        .commit()
                    true
                }
                R.id.create_card_menu_item -> {
                    // Show the home fragment
                    val createCardFragment = CreateCardFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, createCardFragment)
                        .commit()
                    true
                }
                R.id.scan_card_menu_item -> {
                    // Show the profile fragment
                    val scanCardFragment = ScanCardFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, scanCardFragment)
                        .commit()
                    true
                }
                R.id.view_cards_menu_item -> {
                    // Show the settings fragment
                    val viewCardsFragment = ViewCardsFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, viewCardsFragment)
                        .commit()
                    true
                }
                else -> false
            }
        }

    }

}


