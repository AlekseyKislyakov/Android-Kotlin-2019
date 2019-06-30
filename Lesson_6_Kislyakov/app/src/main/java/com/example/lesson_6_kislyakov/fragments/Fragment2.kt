package com.example.lesson_6_kislyakov.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lesson_6_kislyakov.models.Counter
import com.example.lesson_6_kislyakov.adapters.CounterAdapter
import com.example.lesson_6_kislyakov.R
import com.example.lesson_6_kislyakov.utils.SearchResultItemDecoration
import kotlinx.android.synthetic.main.fragment_2.view.*

/**
 * A simple [Fragment] subclass.
 *
 */
class Fragment2 : Fragment() {

    companion object {
        fun newInstance(): Fragment2 {
            return Fragment2()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):
            View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_2, container, false)
        val toolbar = view.toolbarFragment2
        toolbar.inflateMenu(R.menu.menu_toolbar_item2)

        toolbar.setOnMenuItemClickListener {
            when {
                it.itemId == R.id.itemLamp -> Toast.makeText(
                    context,
                    "LAMP",
                    Toast.LENGTH_SHORT
                ).show()
            }
            true
        }

        //create element list and list of spannable strings
        val elements = ArrayList<Counter>()

        //add countersList
        elements.add(
            Counter(
                "Холодная вода", "88005553535", arrayListOf("Ночь", "Всего", "Lad"),
                "", "25.03.18", R.drawable.ic_water_cold
            )
        )
        elements.add(
            Counter(
                "Холодная вода", "88005553535", arrayListOf("Ночь", "Всего"),
                "", "25.03.18", R.drawable.ic_water_hot
            )
        )
        elements.add(
            Counter(
                "Холодная вода", "88005553535", arrayListOf("Ночь"),
                "16.02.18", "25.02.18", R.drawable.ic_electro_copy
            )
        )

        // set up recyclerview
        val adapterRows = CounterAdapter(elements)
        view.recyclerViewTile.layoutManager = LinearLayoutManager(activity)
        view.recyclerViewTile.adapter = adapterRows

        //add decorator with dp margin between items
        view.recyclerViewTile.addItemDecoration(
            SearchResultItemDecoration(
                resources.getDimension(R.dimen.decorator_padding).toInt()
            )
        )
        // Inflate the layout for this fragment
        return view
    }



}


