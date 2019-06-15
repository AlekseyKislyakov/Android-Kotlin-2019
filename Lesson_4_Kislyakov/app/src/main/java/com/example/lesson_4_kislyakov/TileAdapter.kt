package com.example.lesson_4_kislyakov

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.base_info_item.view.*
import kotlinx.android.synthetic.main.detail_info_item.view.*

class TileAdapter(val itemsCount: Int, parent: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //set constants
    internal val DESCRIPTION_RED = 1
    internal val ONEINTWO = 2
    internal val BASECELL = 3
    internal val DEFAULT = 0

    var onItemClick: ((Tile) -> Unit) ? = null

    //create lists for headers, descriptions, avatars
    val size = itemsCount
    val headersDetail: ArrayList<String> = ArrayList()
    val headersBase: ArrayList<String> = ArrayList()

    val descriptionsDetail: ArrayList<String> = ArrayList()

    val avatarsDetail: ArrayList<Int> = ArrayList()
    val avatarsBase: ArrayList<Int> = ArrayList()

    fun addData(parent: Context) {
        headersDetail.addAll(parent.resources.getStringArray(R.array.headers))
        headersBase.addAll(parent.resources.getStringArray(R.array.headers_base))

        descriptionsDetail.addAll(parent.resources.getStringArray(R.array.descriptions))

        val tempAvatarsBase = parent.resources.obtainTypedArray(R.array.avatars_base)
        val tempAvatarsDetail = parent.resources.obtainTypedArray(R.array.avatars)

        for (i in 0..tempAvatarsDetail.length()-1) {
            avatarsDetail.add(tempAvatarsDetail.getResourceId(i,-1))
        }

        for (i in 0..tempAvatarsBase.length()-1) {
            avatarsBase.add(tempAvatarsBase.getResourceId(i,-1))
        }

        tempAvatarsBase.recycle()
        tempAvatarsDetail.recycle()
    }
    init {
        addData(parent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == BASECELL || viewType == ONEINTWO) {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.base_info_item, null)

            val viewHolder = BaseViewHolder(view)

            return viewHolder


        } else {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.detail_info_item, null)

            val viewHolder = DetailViewHolder(view)

            return viewHolder
        }
    }

    override fun getItemCount(): Int {
        return size
    }

    override fun getItemViewType(position: Int): Int {
        // условие для определения айтем какого типа выводить в конкретной позиции
        if (position == 0 || position == 1) return DESCRIPTION_RED
        else if (position == 6) return ONEINTWO
        return if (position >= 7) BASECELL else DEFAULT
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = this@TileAdapter.getItemViewType(position)

        when (type) {
            DESCRIPTION_RED -> {
                val viewHolder = holder as DetailViewHolder
                viewHolder.bind(Tile(headersDetail[position], descriptionsDetail[position],
                        avatarsDetail[position],true))
            }
            ONEINTWO -> {
                val viewHolder = holder as BaseViewHolder
                viewHolder.bind(Tile(headersDetail[position], descriptionsDetail[position],
                        avatarsDetail[position]))
            }
            BASECELL -> {
                val viewHolder = holder as BaseViewHolder
                viewHolder.bind(Tile(headersBase[position-7], null,
                        avatarsBase[position-7]))
            }
            else -> {
                val viewHolder = holder as DetailViewHolder
                viewHolder.bind(Tile(headersDetail[position], descriptionsDetail[position],
                        avatarsDetail[position],false))
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
        }
    }

    inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(element: Tile) {
            itemView.setOnClickListener {
                onItemClick?.invoke(element)
            }

            itemView.textViewBaseInfoTitle?.text = element.header
            if (element.description == null) {
                itemView.textViewBaseInfoDescription?.visibility = View.GONE
            } else {
                itemView.textViewBaseInfoDescription?.text = element.description
            }
            itemView.imageViewBaseInfo?.setBackgroundResource(element.avatar)
            if (element.red)
                itemView.textViewBaseInfoDescription?.setTextColor(Color.parseColor("#FF0000"))
        }
    }
}