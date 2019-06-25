package com.example.lesson_6_kislyakov


import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_2.view.*

/**
 * A simple [Fragment] subclass.
 *
 */
class Fragment2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):
            View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_2, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbarFragment2)
        toolbar.inflateMenu(R.menu.menu_toolbar_item2)

        toolbar.setOnMenuItemClickListener {
            when {
                it.itemId == R.id.itemLamp -> Toast.makeText(
                    activity,
                    "LAMP",
                    Toast.LENGTH_SHORT
                ).show()
            }
            true
        }

        //create element list and list of spannable strings
        val elements = ArrayList<Row>()
        val spannableList = ArrayList<Spannable>()

        // create spannable object
        // make it RED for the all the length
        var spannable = SpannableString("Необходимо подать показания до 25.03.18")
        spannable.setSpan(ForegroundColorSpan(Color.RED), 0, spannable.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)

        //add 2 times (for the first and for the second row in recyclerview)
        spannableList.add(spannable)
        spannableList.add(spannable)

        //create string with bold dates
        var boldString = "Показания сданы 16.02.18 и будут учтены при следующем расчете 25.02.18"

        //reassign value with new string
        spannable = SpannableString(boldString)

        // create regex that will understand date in format dd.mm.yy
        val regex: Regex = "\\d{2}\\.\\d{2}\\.\\d{2}".toRegex()

        // find all matches and set bold spans for the dates in string
        val matches = regex.findAll(boldString)
        val names = matches.map {
            spannable.setSpan(
                StyleSpan(Typeface.BOLD), it.range.first, it.range.last+1,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
        }.joinToString()
        // finally add string with bold dates
        spannableList.add(spannable)

        //add elements
        elements.add(
            Row(
                "Холодная вода", "88005553535", arrayListOf("Ночь", "Всего", "Lad"),
                spannableList[0], R.drawable.ic_water_cold
            )
        )
        elements.add(
            Row(
                "Холодная вода", "88005553535", arrayListOf("Ночь", "Всего"),
                spannableList[1], R.drawable.ic_water_hot
            )
        )
        elements.add(
            Row(
                "Холодная вода", "88005553535", arrayListOf("Ночь"),
                spannableList[2], R.drawable.ic_electro_copy
            )
        )

        // set up recyclerview
        val adapterRows = RowsAdapter(elements)
        view.recyclerViewTile.layoutManager = LinearLayoutManager(activity)
        view.recyclerViewTile.adapter = adapterRows

        //add decorator with dp margin between items
        view.recyclerViewTile.addItemDecoration(
                SearchResultItemDecoration(
                        8.dp
                )
        )
        // Inflate the layout for this fragment
        return view
    }

    val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

}


