package com.amitdev.tap_n_connect

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A fragment representing a list of Items.
 */
class ViewCardsFragment : Fragment() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_view_cards_list, container, false)
        val sharedPreferencesHelper = SharedPreferencesHelper(requireContext())
        val cardList = getCardListFromSP(sharedPreferencesHelper)
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyCardRecyclerViewAdapter(cardList = cardList)
            }
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    private fun getCardListFromSP(sharedPreferencesHelper: SharedPreferencesHelper): List<Card> {
        //Temporary
        /*val dummyCardList = listOf<Card>(
            Card(
                name = "Amit Levy",
                jobTitle = "Mobile SDK",
                phoneNumber = "0545455692",
                email = "amit@mail.com",
                company = "AppsFlyer",
                address = "",
                website = ""
            ),
            Card(
                name = "Rachel Leon",
                jobTitle = "ux/ui",
                phoneNumber = "0546599874",
                email = "rachel@mail.com",
                company = "AppsFlyer",
                address = "",
                website = ""
            ),
            Card(
                name = "Gal Shamir",
                jobTitle = "Software Engineer",
                phoneNumber = "0540943123",
                email = "gal@mail.com",
                company = "AppsFlyer",
                address = "",
                website = ""
            ),
            Card(
                name = "Dane Flora",
                jobTitle = "Customer Success Manager",
                phoneNumber = "0543255348",
                email = "dane@mail.com",
                company = "AppsFlyer",
                address = "",
                website = ""
            )
        )*/
        val cards = sharedPreferencesHelper.getCards().toMutableList()
//        cards.addAll(dummyCardList)
//        sharedPreferencesHelper.saveCards(cards)
//        Log.d("Tappy", "getCardListFromSP: ${cards}")
        return cards

    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            ViewCardsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}