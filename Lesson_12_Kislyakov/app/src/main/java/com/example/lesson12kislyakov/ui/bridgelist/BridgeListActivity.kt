package com.example.lesson12kislyakov.ui.bridgelist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lesson12kislyakov.R
import com.example.lesson12kislyakov.data.models.Bridge
import com.example.lesson12kislyakov.ui.base.BaseActivity
import com.example.lesson12kislyakov.ui.bridgemap.MapsActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class BridgeListActivity : BaseActivity(), BridgeListMvpView {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, BridgeListActivity::class.java)
        }
    }

    private val STATE_LOADING = 0
    private val STATE_DATA = 1
    private val STATE_ERROR = 2

    @Inject
    lateinit var mMainPresenter: BridgeListPresenter
    @Inject
    lateinit var mBridgesAdapter: BridgeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityComponent()?.inject(this)

        recyclerViewMain.adapter = mBridgesAdapter
        recyclerViewMain.layoutManager = LinearLayoutManager(this)

        mMainPresenter.attachView(this)
        mMainPresenter.loadBridges()

        toolbarMain.inflateMenu(R.menu.bridge_list_menu)
        toolbarMain.setOnMenuItemClickListener {
            when {
                it.itemId == R.id.itemMap ->
                    startActivity(MapsActivity.newIntent(this))
            }
            true
        }

        buttonRetry.setOnClickListener { mMainPresenter.loadBridges() }
    }

    override fun showLoadingError() {
        viewFlipper.displayedChild = STATE_ERROR
    }

    override fun showBridges(bridges: List<Bridge>) {
        mBridgesAdapter.setItems(bridges)
        viewFlipper.displayedChild = STATE_DATA
    }

    override fun showSingleBridge(bridge: Bridge) {

    }

    override fun showProgressView() {
        viewFlipper.displayedChild = STATE_LOADING
    }
}
