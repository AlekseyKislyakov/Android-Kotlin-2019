package com.example.lesson12kislyakov.ui.bridgelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson12kislyakov.R
import com.example.lesson12kislyakov.data.models.Bridge
import com.example.lesson12kislyakov.ui.bridgedetail.BridgeDetailActivity
import com.example.lesson12kislyakov.utils.DivorceUtil
import kotlinx.android.synthetic.main.bridge_row.view.*
import javax.inject.Inject

class BridgeListAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var bridgeList: List<Bridge> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.bridge_row, parent, false)

        return BridgeListViewHolder(view)
    }

    fun setItems(elements: List<Bridge>) {
        bridgeList = elements
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return bridgeList.size

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as BridgeListViewHolder
        viewHolder.bind(bridgeList[position])
    }

    inner class BridgeListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(element: Bridge) {
            itemView.setOnClickListener {
                itemView.context.startActivity(BridgeDetailActivity.newIntent(itemView.context, element.id))
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