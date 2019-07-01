package com.example.lesson7kislyakov.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lesson7kislyakov.*
import com.example.lesson7kislyakov.adapters.BridgeListAdapter
import com.example.lesson7kislyakov.models.BridgeDetailInfo
import com.example.lesson7kislyakov.network.BridgeApiService
import com.example.lesson7kislyakov.utils.DivorceUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var disposable: Disposable? = null
    val bridgeListAdapter = BridgeListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        disposable = BridgeApiService.create()
            .search()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                result ->
                bridgeListAdapter.setItems(result.objects)
                bridgeListAdapter.onItemClick = { item ->

                    val divorceUtil = DivorceUtil()
                    val stateBridge = divorceUtil.divorceState(item.divorces)
                    val bridgeInfoObject: BridgeDetailInfo

                    // check if bridge opened or closed then send photo's uri
                    bridgeInfoObject = if(stateBridge == divorceUtil.STATE_CLOSED){
                        BridgeDetailInfo(
                            item.name,
                            divorceUtil.divorceListToString(item.divorces),
                            item.description,
                            item.photo_close,
                            stateBridge
                        )
                    } else {
                        BridgeDetailInfo(
                            item.name,
                            divorceUtil.divorceListToString(item.divorces),
                            item.description,
                            item.photo_open,
                            stateBridge
                        )
                    }

                    startActivity(
                        DetailBridgeActivity.newIntent(
                            this,
                            bridgeInfoObject
                        )
                    )
                }
                recyclerViewMain.layoutManager = LinearLayoutManager(this)
                recyclerViewMain.adapter = bridgeListAdapter
            }, { error ->
                error.printStackTrace()
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        if(disposable!!.isDisposed){
            disposable!!.dispose()
        }

    }
}
