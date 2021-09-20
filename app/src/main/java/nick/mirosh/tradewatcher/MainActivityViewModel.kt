package nick.mirosh.tradewatcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import nick.mirosh.tradewatcher.entity.TradeView
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import tradewatcher.BuildConfig

class MainActivityViewModel : ViewModel() {

    private val tag = MainActivityViewModel::class.java.simpleName

    private val _tradesUI = MutableStateFlow(listOf<TradeView>())
    val tradesUI: StateFlow<List<TradeView>> = _tradesUI

    private var webSocket: WebSocket
    private var echoWebSocketListener: EchoWebSocketListener


    init {

        val client = OkHttpClient()
        val request =
            Request.Builder().url("${BuildConfig.WEBSOCKET_URL}?token=${BuildConfig.API_KEY}")
                .build()
        echoWebSocketListener = EchoWebSocketListener()
        webSocket = client.newWebSocket(request, echoWebSocketListener)
        client.dispatcher.executorService.shutdown()
        establishConnection()
    }


    private fun establishConnection() {
        viewModelScope.launch {
            echoWebSocketListener
                .socketEventChannel
                .buffer(10)
                .collect {
                    _tradesUI.value = it.map { trade -> trade.toTradeView() }
                }
        }
    }

    fun search(text: String) {
        if (text.uppercase() == "BTC") {
            webSocket.send("{\"type\":\"subscribe\",\"symbol\":\"BINANCE:BTCUSDT\"}")
        }
        else {
            webSocket.send("{\"type\":\"subscribe\",\"symbol\":\"${text.uppercase()}\"}")
        }
    }

}