package com.example.lesson12kislyakov.ui.bridgedetail

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import com.bumptech.glide.Glide
import com.example.lesson12kislyakov.R
import com.example.lesson12kislyakov.data.models.Bridge
import com.example.lesson12kislyakov.data.remote.NetworkService.Factory.BASE_URL
import com.example.lesson12kislyakov.ui.base.BaseActivity
import com.example.lesson12kislyakov.utils.DivorceUtil
import kotlinx.android.synthetic.main.activity_bridge_detail.*
import javax.inject.Inject

class BridgeDetailActivity : BaseActivity(), BridgeDetailMvpView {
    private val divorceUtil = DivorceUtil()

    private val STATE_LOADING = 0
    private val STATE_DATA = 1
    private val STATE_ERROR = 2

    companion object {
        private val INTENT_BRIDGE_INFO = "bridge_info"

        fun newIntent(context: Context, bridgeId: Int): Intent {
            val intent = Intent(context, BridgeDetailActivity::class.java)
            intent.putExtra(INTENT_BRIDGE_INFO, bridgeId)
            return intent
        }
    }

    @Inject
    lateinit var mBridgeDetailPresenter: BridgeDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bridge_detail)

        activityComponent()?.inject(this)
        mBridgeDetailPresenter.attachView(this)

        toolbarDetailBridge.setNavigationOnClickListener { super.onBackPressed() }

        if (intent.hasExtra(INTENT_BRIDGE_INFO)) {
            mBridgeDetailPresenter.loadSingleBridge(intent.getIntExtra(INTENT_BRIDGE_INFO, 0))
        }
    }

    override fun showBridges(bridges: List<Bridge>) {
    }

    override fun showSingleBridge(bridge: Bridge) {
        textViewDetailHeader.text = bridge.name
        textViewDetailDivorce.text = divorceUtil.divorceListToString(bridge.divorces)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textViewDetailDescription.text = Html.fromHtml(bridge.description, Html.FROM_HTML_MODE_COMPACT)
        } else {
            textViewDetailDescription.text = Html.fromHtml(bridge.description)
        }

        imageViewDetailState.setImageResource(divorceUtil.setStatus(bridge.divorces))

        if (divorceUtil.divorceState(bridge.divorces) != divorceUtil.STATE_CLOSED) {
            Glide.with(this)
                    .load(BASE_URL + bridge.photo_open)
                    .into(imageViewToolbarDetailBackground)
        } else {
            Glide.with(this)
                    .load(BASE_URL + bridge.photo_close)
                    .into(imageViewToolbarDetailBackground)
        }
        viewFlipperDetail.displayedChild = STATE_DATA
    }

    override fun showProgressView() {
        viewFlipperDetail.displayedChild = STATE_LOADING
    }

    override fun showLoadingError() {
        viewFlipperDetail.displayedChild = STATE_ERROR
        buttonRetryDetail.setOnClickListener {
            mBridgeDetailPresenter.loadSingleBridge(intent.getIntExtra(INTENT_BRIDGE_INFO, 5))
        }
    }
}
