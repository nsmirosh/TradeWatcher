package nick.mirosh.tradewatcher

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import nick.mirosh.tradewatcher.entity.Trade
import nick.mirosh.tradewatcher.entity.TradeResponse
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class EchoWebSocketListener : WebSocketListener() {

    private val tag = EchoWebSocketListener::class.java.simpleName

    val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val _socketEventChannel = MutableStateFlow((listOf<Trade>()))
    val socketEventChannel: StateFlow<List<Trade>> = _socketEventChannel

    override fun onOpen(webSocket: WebSocket, response: Response) {

    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.w(tag, "text from sockets = $text")
        val message = parseMessage(text)
        Log.w(tag, "message = $message")

        if (message != null) {
            _socketEventChannel.value = message.data!!
        }
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.w(tag, "text from sockets = ${bytes.hex()}")
//            val message = parseMessage(bytes.hex())
//            _socketEventChannel.value = bytes.hex()
    }


    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
//            _socketEventChannel.value = "Closing : $code / $reason"

    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
        super.onFailure(webSocket, t, response)
//            _socketEventChannel.value = "Error : ${t.message}"

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