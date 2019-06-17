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

    private val TILE_SIZE: Int = 1
    private val ROW_SIZE: Int = 2

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
                2, elements.size
            )
        )

        //set up recyclerview
        recyclerViewTile.layoutManager = layoutManager
        recyclerViewTile.adapter = adapter
    }

    fun addData() {
        elements.add(Tile("Квитанции", "-40 080,55 \u20BD", R.drawable.ic_bill, TILE_SIZE, true))
        elements.add(Tile("Счетчики", "Подайте показания", R.drawable.ic_counter, TILE_SIZE, true))
        elements.add(Tile("Рассрочка", "Сл. платеж 25.01.2018", R.drawable.ic_installment, TILE_SIZE, false))
        elements.add(Tile("Страхование", "Полис до 12.01.2019", R.drawable.ic_insurance, TILE_SIZE, false))
        elements.add(Tile("Интернет и ТВ", "Баланск 350 \u20BD", R.drawable.ic_tv, TILE_SIZE, false))
        elements.add(Tile("Домофон", "Подключен", R.drawable.ic_homephone, TILE_SIZE, false))
        elements.add(Tile("Охрана", "Нет", R.drawable.ic_guard, ROW_SIZE, false))
        elements.add(Tile("Контакты УК и служб", null, R.drawable.ic_uk_contacts, ROW_SIZE, false))
        elements.add(Tile("Мои заявки", null, R.drawable.ic_request, ROW_SIZE, false))
        elements.add(Tile("Памятка жителя А101", null, R.drawable.ic_about, ROW_SIZE, false))
    }
}
