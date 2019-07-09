package com.example.lesson8kislyakov.adapters

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson8kislyakov.R
import kotlinx.android.synthetic.main.color_picker_element_layout.view.*

const val STATE_UNDERFINED = -2
const val STATE_NULL = -3

// Below is the class of magic

class ColorPickerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null

    // check position is the position of current white check inside the color circle
    // delete check position is the position to delete check if other is chosen
    var checkPosition = STATE_UNDERFINED
    var deleteCheckPosition = STATE_NULL

    private var colorList: List<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.color_picker_element_layout, parent, false)
        return ColorListViewHolder(view)
    }

    fun setItems(elements: List<String>, defaultColor: String) {
        colorList = elements
        // search if color already exists in palette, then put check on it
        checkPosition = colorList.indexOf(defaultColor)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return colorList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ColorListViewHolder
        viewHolder.bind(colorList[position])

    }

    inner class ColorListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(element: String) {
            // this is to delete previous checks
            if (adapterPosition == deleteCheckPosition) {
                itemView.imageViewColorPicker.setImageResource(android.R.color.transparent)
            }

            // this is to put check on chosen position on the first time (for already existing color)
            if(adapterPosition == checkPosition){
                itemView.imageViewColorPicker.setImageResource(R.drawable.ic_check_black_24dp)

            }


            itemView.setOnClickListener {
                // set check on click
                itemView.imageViewColorPicker.setImageResource(R.drawable.ic_check_black_24dp)
                // check if we do not check again the same color
                if (checkPosition != deleteCheckPosition) {
                    // apply delete position
                    deleteCheckPosition = checkPosition
                    // delete previous Item and invoke color element
                    notifyItemChanged(deleteCheckPosition)
                    onItemClick?.invoke(element)
                }
                checkPosition = if (checkPosition == adapterPosition) {
                    // else set color to white and delete check
                    notifyItemChanged(deleteCheckPosition)
                    onItemClick?.invoke(itemView.context.getString(R.string.color_white_string))
                    STATE_UNDERFINED
                } else
                    // otherwise change to adapter position and leave check
                    adapterPosition

            }

            when (val background = itemView.imageViewColorPicker.background) {
                is GradientDrawable -> {
                    // cast to 'GradientDrawable'
                        background.setColor(Color.parseColor(element))

                }

            }

        }
    }
}