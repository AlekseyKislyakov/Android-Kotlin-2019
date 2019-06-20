package com.example.lesson_4_kislyakov

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var elements: ArrayList<Tile> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        addData()
        //set up toolbar
        toolbar.inflateMenu(R.menu.menu_main)

        //setting up on back(home & info) click listeners
        toolbar.setNavigationOnClickListener {
            Toast.makeText(this, "BACK", Toast.LENGTH_SHORT).show()
        }

        //setting up on menu(home & info) click listeners
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.itemToolbarHome) {
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
            } else if (it.itemId == R.id.itemToolbarInfo) {
                AlertDialog.Builder(this)
                    .setTitle("Info")
                    .setMessage("Hello")
                    .setPositiveButton("hi", null)
                    .create()
                    .show()
            }
            true
        }

        //create adapter with 10 items
        val adapter = TileAdapter(elements)

        //set onClickListener to items in recyclerview
        //call is in the adapter function
        adapter.onItemClick = { contact ->
            Snackbar.make(window.decorView.rootView, contact.header, Snackbar.LENGTH_SHORT).show()
        }

        //set up layout manager with 2 columns
        val layoutManager = GridLayoutManager(this, 2)

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    adapter.ROW_SIZE -> 2
                    adapter.TILE_SIZE -> 1
                    else -> -1
                }
            }
        }

        //add decorations
        recyclerViewTile.addItemDecoration(
            SearchResultItemDecoration(
                resources.getDimensionPixelSize(R.dimen.photos_list_spacing),
                2
            )
        )

        //set up recyclerview
        recyclerViewTile.layoutManager = layoutManager
        recyclerViewTile.adapter = adapter
    }

    fun addData() {
        //firstly add detail items
        elements.add(TileDetail("Квитанции", "-40 080,55 \u20BD", R.drawable.ic_bill, true))
        elements.add(TileDetail("Счетчики", "Подайте показания", R.drawable.ic_counter,  true))
        elements.add(TileDetail("Рассрочка", "Сл. платеж 25.01.2018", R.drawable.ic_installment,  false))
        elements.add(TileDetail("Страхование", "Полис до 12.01.2019", R.drawable.ic_insurance,  false))
        elements.add(TileDetail("Интернет и ТВ", "Баланск 350 \u20BD", R.drawable.ic_tv,  false))
        elements.add(TileDetail("Домофон", "Подключен", R.drawable.ic_homephone,  false))
        elements.add(TileDetail("Охрана", "Нет", R.drawable.ic_guard,  false))

        //add base items
        elements.add(TileBase("Контакты УК и служб", null, R.drawable.ic_uk_contacts,  false))
        elements.add(TileBase("Мои заявки", null, R.drawable.ic_request,  false))
        elements.add(TileBase("Памятка жителя А101", null, R.drawable.ic_about,  false))
    }
}
