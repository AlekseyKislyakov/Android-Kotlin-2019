package com.example.lesson7kislyakov.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.lesson7kislyakov.network.BridgeApiService

import com.example.lesson7kislyakov.utils.DivorceUtil
import com.example.lesson7kislyakov.R
import com.example.lesson7kislyakov.models.Bridge
import kotlinx.android.synthetic.main.activity_detail_bridge.*

class DetailBridgeActivity : AppCompatActivity() {

    companion object {
        private val INTENT_BRIDGE_INFO = "bridge_info"

        fun newIntent(context: Context, bridge: Bridge): Intent {
            val intent = Intent(context, DetailBridgeActivity::class.java)
            intent.putExtra(INTENT_BRIDGE_INFO, bridge)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_bridge)

        val divorceUtil = DivorceUtil()

        toolbarDetailBridge.setNavigationOnClickListener { super.onBackPressed() }

        // get info from intent
        if (intent.hasExtra(INTENT_BRIDGE_INFO)) {
            val bridgeDetailInfo = intent.getParcelableExtra<Bridge>(INTENT_BRIDGE_INFO)

            textViewDetailHeader.text = bridgeDetailInfo.name
            textViewDetailDivorce.text = divorceUtil.divorceListToString(bridgeDetailInfo.divorces)

            //check bridge state then put status image
            when (divorceUtil.divorceState(bridgeDetailInfo.divorces)) {
                divorceUtil.STATE_OPEN -> {
                    imageViewDetailState?.setImageResource(R.drawable.ic_brige_normal)
                    Glide.with(this)
                        .load(BridgeApiService.BASE_URL + bridgeDetailInfo.photo_open)
                        .into(imageViewToolbarDetailBackground)
                }
                divorceUtil.STATE_NEAR -> {
                    imageViewDetailState?.setImageResource(R.drawable.ic_brige_soon)
                    Glide.with(this)
                        .load(BridgeApiService.BASE_URL + bridgeDetailInfo.photo_open)
                        .into(imageViewToolbarDetailBackground)
                }
                divorceUtil.STATE_CLOSED -> {
                    imageViewDetailState?.setImageResource(R.drawable.ic_brige_late)
                    Glide.with(this)
                        .load(BridgeApiService.BASE_URL + bridgeDetailInfo.photo_close)
                        .into(imageViewToolbarDetailBackground)
                }
            }

            // load into toolbar already checked (normal or closed) bridge picture


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textViewDetailDescription.text = Html.fromHtml(bridgeDetailInfo.description, Html.FROM_HTML_MODE_COMPACT)
            } else {
                textViewDetailDescription.text = Html.fromHtml(bridgeDetailInfo.description)
            }
        }
    }
}
