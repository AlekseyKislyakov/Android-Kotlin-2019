package com.example.lesson_6_kislyakov.adapters

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson_6_kislyakov.R
import com.example.lesson_6_kislyakov.models.Counter
import kotlinx.android.synthetic.main.recyclerview_counter.view.*


class CounterAdapter(val countersList: ArrayList<Counter>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val FIRST_PARAMETER = 0
    val SECOND_PARAMETER = 1
    val THIRD_PARAMETER = 2

    val PARAMETERS_COUNT_3 = 3
    val PARAMETERS_COUNT_2 = 2
    val PARAMETERS_COUNT_1 = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recyclerview_counter, parent, false)
        return CounterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return countersList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as CounterViewHolder
        viewHolder.bind(countersList[position])
    }

    inner class CounterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(counter: Counter) {
            itemView.textViewRowHeader?.text = counter.header
            itemView.textViewRowSerial?.text = counter.serial

            when {
                counter.data.size == PARAMETERS_COUNT_3 -> {
                    itemView.textViewRowInputFirst?.text = counter.data[FIRST_PARAMETER]
                    itemView.textViewRowInputSecond?.text = counter.data[SECOND_PARAMETER]
                    itemView.textViewRowInputThird?.text = counter.data[THIRD_PARAMETER]
                }
                counter.data.size == PARAMETERS_COUNT_2 -> {
                    itemView.textViewRowInputFirst?.text = counter.data[FIRST_PARAMETER]
                    itemView.textViewRowInputSecond?.text = counter.data[SECOND_PARAMETER]
                    itemView.textViewRowInputThird?.visibility = View.GONE
                    itemView.editTextRowInputThird?.visibility = View.GONE
                }
                else -> {
                    itemView.textViewRowInputFirst?.text = counter.data[FIRST_PARAMETER]
                    itemView.textViewRowInputSecond?.visibility = View.GONE
                    itemView.textViewRowInputThird?.visibility = View.GONE
                    itemView.editTextRowInputSecond?.visibility = View.GONE
                    itemView.editTextRowInputThird?.visibility = View.GONE
                }
            }

            // here I check if parameters were not given (empty string)
            // string becomes RED and alert icon is shown
            // else if user gave date in time, given date and expire dates are shown

            if (counter.givenDate == "") {
                var descriptionSpannableString =
                    SpannableString(
                        itemView.resources.getString(R.string.counter_data_tobe_provided) + " " +
                                counter.expireDate
                    )
                descriptionSpannableString.setSpan(
                    ForegroundColorSpan(Color.RED),
                    0, descriptionSpannableString.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
                itemView.textViewRowDescription.text = descriptionSpannableString
                itemView.textViewRowDescription.compoundDrawablePadding =
                    itemView.resources.getDimension(R.dimen.alert_padding).toInt()
                itemView.textViewRowDescription.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(itemView.context,
                        R.drawable.ic_alert
                    ),
                    null, null, null
                )
            } else {
                var descriptionSpannableString =
                    SpannableString(
                        itemView.resources.getString(R.string.counter_data_given_begin) + " " +
                                counter.givenDate + " " + itemView.resources.getString(R.string.counter_data_given_end) + " " +
                                counter.expireDate
                    )

                val regex: Regex = "\\d{2}\\.\\d{2}\\.\\d{2}".toRegex()
                val matches = regex.findAll(descriptionSpannableString)
                matches.map {
                    descriptionSpannableString.setSpan(
                        StyleSpan(Typeface.BOLD), it.range.first, it.range.last + 1,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE
                    )
                }.joinToString()
                itemView.textViewRowDescription?.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                itemView.textViewRowDescription.text = descriptionSpannableString
            }

            itemView.imageViewRowAvatar?.setImageResource(counter.avatar)

            itemView.imageButtonRowInfo.setOnClickListener {
                Toast.makeText(itemView.context, "Info " + counter.header, Toast.LENGTH_SHORT)
                    .show()
            }

            itemView.imageButtonRowMore.setOnClickListener {
                Toast.makeText(itemView.context, "More " + counter.header, Toast.LENGTH_SHORT)
                    .show()
            }

            itemView.imageButtonRowSend.setOnClickListener {
                Toast.makeText(itemView.context, "Send " + counter.header, Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }
}