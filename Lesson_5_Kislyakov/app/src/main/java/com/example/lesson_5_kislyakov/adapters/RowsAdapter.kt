package com.example.lesson_5_kislyakov.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lesson_5_kislyakov.R
import com.example.lesson_5_kislyakov.models.Row
import kotlinx.android.synthetic.main.horizontal_row.view.*


class RowsAdapter(val elements: ArrayList<Row>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClick: ((Row) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.horizontal_row, null)
        return RowViewHolder(view)
    }

    override fun getItemCount(): Int {
        return elements.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as RowViewHolder
        viewHolder.bind(elements[position])
    }

    inner class RowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(element: Row) {
            itemView.setOnClickListener {
                onItemClick?.invoke(element)
            }

            itemView.textViewRowHeader?.text = element.header

            itemView.textViewRowText?.text = element.description
            itemView.textViewRowAddress?.text = element.address

            Glide.with(itemView)
                .load(element.avatar)
                .into(itemView.imageViewRowAvatar)

            itemView.imageViewRowMore.setOnClickListener {
                Toast.makeText(itemView.context, "More "+ element.header, Toast.LENGTH_SHORT).show()
            }
        }
    }
}