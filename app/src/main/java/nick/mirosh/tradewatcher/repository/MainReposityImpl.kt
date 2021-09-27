package nick.mirosh.tradewatcher.repository

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch
import nick.mirosh.tradewatcher.EchoWebSocketListener
import nick.mirosh.tradewatcher.entity.Trade
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import tradewatcher.BuildConfig

class MainReposityImpl: MainRepository {

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
    }


    override fun getTrades(): StateFlow<List<Trade>> {
            return echoWebSocketListener
                .socketEventChannel
        }

    override fun getUpdates(stockName: String) {
        if (stockName.uppercase() == "BTC") {
            webSocket.send("{\"type\":\"subscribe\",\"symbol\":\"BINANCE:BTCUSDT\"}")
        }
        else {
            webSocket.send("{\"type\":\"subscribe\",\"symbol\":\"${stockName.uppercase()}\"}")
        }
    }
}