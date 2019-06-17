package com.example.lesson_4_kislyakov

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.base_info_item.view.*
import kotlinx.android.synthetic.main.detail_info_item.view.*

class TileAdapter(val elements: ArrayList<Tile>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //set constants
    val TILE_SIZE: Int = 1
    val ROW_SIZE: Int = 2

    var onItemClick: ((Tile) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == ROW_SIZE) {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.base_info_item, null)

            BaseViewHolder(view)

        } else {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.detail_info_item, null)

            DetailViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return elements.size
    }

    override fun getItemViewType(position: Int): Int {
        // условие для определения айтем какого типа выводить в конкретной позиции
        return elements[position].size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (this@TileAdapter.getItemViewType(position)) {
            TILE_SIZE -> {
                val viewHolder = holder as DetailViewHolder
                viewHolder.bind(elements[position])
            }
            ROW_SIZE -> {
                val viewHolder = holder as BaseViewHolder
                viewHolder.bind(elements[position])
            }
        }
    }

    inner class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(element: Tile) {
            itemView.setOnClickListener {
                onItemClick?.invoke(element)
            }
            itemView.textViewDetailInfoTitle?.text = element.header
            itemView.textViewDetailInfoDescription?.text = element.description
            itemView.imageViewDetailInfo?.setBackgroundResource(element.avatar)
            if (element.red)
                itemView.textViewDetailInfoDescription?.setTextColor(Color.parseColor("#FF0000"))
            else
                itemView.textViewDetailInfoDescription?.setTextColor(Color.parseColor("#888888"))
        }
    }

    inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(element: Tile) {
            itemView.setOnClickListener {
                onItemClick?.invoke(element)
            }

            itemView.textViewBaseInfoTitle?.text = element.header
            if (element.description == null)
                itemView.textViewBaseInfoDescription?.visibility = View.GONE
            else{
                itemView.textViewBaseInfoDescription?.visibility = View.VISIBLE
                itemView.textViewBaseInfoDescription?.text = element.description
            }

            itemView.imageViewBaseInfo?.setBackgroundResource(element.avatar)

            if (element.red)
                itemView.textViewBaseInfoDescription?.setTextColor(Color.parseColor("#FF0000"))
            else
                itemView.textViewBaseInfoDescription?.setTextColor(Color.parseColor("#888888"))
        }
    }
}