package com.example.lesson7kislyakov.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson7kislyakov.R
import com.example.lesson7kislyakov.models.Bridge
import com.example.lesson7kislyakov.utils.DivorceUtil
import kotlinx.android.synthetic.main.bridge_row.view.*

class BridgeListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClick: ((Bridge) -> Unit)? = null

    private var bridgeList: List<Bridge>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.bridge_row, parent, false)
        return BridgeListViewHolder(view)
    }

    fun setItems(elements: List<Bridge>){
        bridgeList = elements
    }

    override fun getItemCount(): Int {
        if(bridgeList != null){
            return bridgeList!!.size
        } else throw Exception("Must use setItems()!")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as BridgeListViewHolder
        viewHolder.bind(bridgeList!![position])
    }

    inner class BridgeListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(element: Bridge) {
            itemView.setOnClickListener {
                onItemClick?.invoke(element)
            }
            val divorceUtil = DivorceUtil()

            itemView.textViewHeaderRow?.text = element.name

            itemView.textViewDivorceRow?.text = divorceUtil.divorceListToString(element.divorces)

            when (divorceUtil.divorceState(element.divorces)) {
                divorceUtil.STATE_OPEN -> itemView.imageViewBridgeRow?.setImageResource(R.drawable.ic_brige_normal)
                divorceUtil.STATE_NEAR -> itemView.imageViewBridgeRow?.setImageResource(R.drawable.ic_brige_soon)
                divorceUtil.STATE_CLOSED -> itemView.imageViewBridgeRow?.setImageResource(R.drawable.ic_brige_late)
            }

        }
    }
}