package nick.mirosh.tradewatcher

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import nick.mirosh.tradewatcher.entity.Trade
import nick.mirosh.tradewatcher.entity.TradeResponse
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONObject

class EchoWebSocketListener : WebSocketListener() {

    private val tag = EchoWebSocketListener::class.java.simpleName

    val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val _socketEventChannel = MutableStateFlow("")
    val socketEventChannel: StateFlow<String> = _socketEventChannel

    override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {

    }

    @DelicateCoroutinesApi
    override fun onMessage(webSocket: WebSocket, text: String) {
        GlobalScope.launch {
            val message = parseMessage(text)
            _socketEventChannel.value = text
        }
    }

    @DelicateCoroutinesApi
    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        GlobalScope.launch {
            val message = parseMessage(bytes.hex())
            _socketEventChannel.value = bytes.hex()
        }
    }

    @DelicateCoroutinesApi
    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        GlobalScope.launch {
            _socketEventChannel.value = "Closing : $code / $reason"
        }
    }

    @DelicateCoroutinesApi
    override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
        super.onFailure(webSocket, t, response)
        GlobalScope.launch {
            _socketEventChannel.value = "Error : ${t.message}"
        }
    }

    private fun parseMessage(text: String): TradeResponse? {
        val adapter = moshi.adapter(TradeResponse::class.java)

        var trade: TradeResponse? = null
        try {
            trade = adapter.fromJson(text)
        } catch (exception: Exception) {
            Log.e(tag, "parse exception: ${exception.message}")
        }
        Log.w(tag, "trade = $trade")
        return trade
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}