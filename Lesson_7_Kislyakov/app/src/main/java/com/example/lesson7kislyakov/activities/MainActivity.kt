package com.example.lesson7kislyakov.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lesson7kislyakov.R
import com.example.lesson7kislyakov.adapters.BridgeListAdapter
import com.example.lesson7kislyakov.network.BridgeApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val STATE_LOADING = 0
    private val STATE_DATA = 1
    private val STATE_ERROR = 2

    private var disposable: Disposable? = null
    private val bridgeListAdapter = BridgeListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonRetry.setOnClickListener {
            getBridges()
        }
        getBridges()
    }

    private fun getBridges() {
        viewFlipper.displayedChild = STATE_LOADING
        disposable = BridgeApiService.getRetrofit()
            .getSingleBridgeResponse()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ result ->
                bridgeListAdapter.setItems(result.objects)
                bridgeListAdapter.onItemClick = { item ->
                    startActivity(
                        DetailBridgeActivity.newIntent(
                            this,
                            item
                        )
                    )
                }

                recyclerViewMain.layoutManager = LinearLayoutManager(this)
                recyclerViewMain.adapter = bridgeListAdapter
                viewFlipper.displayedChild = STATE_DATA
            }, { error ->
                viewFlipper.displayedChild = STATE_ERROR
                error.printStackTrace()
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (disposable!!.isDisposed) {
            disposable!!.dispose()
        }

    }
}
