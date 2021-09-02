package nick.mirosh.tradewatcher

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

    private val _socketEventChannel = MutableStateFlow("")
    val socketEventChannel: StateFlow<String> = _socketEventChannel

    override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {

    }

    @DelicateCoroutinesApi
    override fun onMessage(webSocket: WebSocket, text: String) {
        GlobalScope.launch {

            _socketEventChannel.value = text
        }
    }

    @DelicateCoroutinesApi
    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        GlobalScope.launch {
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

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}