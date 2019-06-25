package com.example.lesson_6_kislyakov

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.getSpans
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_row.view.*


class RowsAdapter(val elements: ArrayList<Row>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recyclerview_row, parent, false)
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
            itemView.textViewRowHeader?.text = element.header
            itemView.textViewRowSerial?.text = element.serial

            when {
                element.data.size == 3 -> {
                    itemView.textViewRowInput1?.text = element.data[0]
                    itemView.textViewRowInput2?.text = element.data[1]
                    itemView.textViewRowInput3?.text = element.data[2]
                }
                element.data.size == 2 -> {
                    itemView.textViewRowInput1?.text = element.data[0]
                    itemView.textViewRowInput2?.text = element.data[1]
                    itemView.textViewRowInput3?.visibility = View.GONE
                    itemView.editTextRowInput3?.visibility = View.GONE
                }
                else -> {
                    itemView.textViewRowInput1?.text = element.data[0]
                    itemView.textViewRowInput2?.visibility = View.GONE
                    itemView.textViewRowInput3?.visibility = View.GONE
                    itemView.editTextRowInput2?.visibility = View.GONE
                    itemView.editTextRowInput3?.visibility = View.GONE
                }
            }
            var alert = false
            val arraySpans: Array<ForegroundColorSpan> =
                element.description.getSpans(0, 15, ForegroundColorSpan::class.java)
            arraySpans.forEach {
                if(it.foregroundColor == Color.RED){
                    alert = true
                }
            }
            if(!alert){
                itemView.textViewRowDescription?.
                    setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
            itemView.textViewRowDescription?.text = element.description

            itemView.imageViewRowAvatar?.setImageResource(element.avatar)

            itemView.imageButtonRowInfo.setOnClickListener {
                Toast.makeText(itemView.context, "Info " + element.header, Toast.LENGTH_SHORT)
                    .show()
            }

            itemView.imageButtonRowMore.setOnClickListener {
                Toast.makeText(itemView.context, "More " + element.header, Toast.LENGTH_SHORT)
                    .show()
            }

            itemView.imageButtonRowSend.setOnClickListener {
                Toast.makeText(itemView.context, "Send " + element.header, Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }
}