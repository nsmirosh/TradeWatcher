package nick.mirosh.tradewatcher

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import nick.mirosh.tradewatcher.entity.Trade
import nick.mirosh.tradewatcher.entity.TradeResponse
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONObject

class EchoWebSocketListener : WebSocketListener() {

    val socketEventChannel: Channel<String> = Channel(10)

    // Backing property to avoid flow emissions from other classes
    private val _tickFlow = MutableSharedFlow<String>(replay = 0)
    val tickFlow: SharedFlow<String> = _tickFlow


    override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
    }

    @DelicateCoroutinesApi
    override fun onMessage(webSocket: WebSocket, text: String) {
        GlobalScope.launch {
            _tickFlow.emit(text)
        }

        MutableStateFlow(text)
    }

    @DelicateCoroutinesApi
    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        GlobalScope.launch {
            _tickFlow.emit(bytes.hex())
        }
    }

    @DelicateCoroutinesApi
    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        GlobalScope.launch {
            socketEventChannel.send("Closing : $code / $reason")
        }
    }


   /* class Response(json: String) : JSONObject(json) {
        val type: String? = this.optString("type")
        val data = this.optJSONArray("data")
            ?.let { 0.until(it.length()).map { i -> it.optJSONObject(i) } } // returns an array of JSONObject
            ?.map { Trade(it.toString()) } // transforms each JSONObject of the array into Foo
    }*/


    @DelicateCoroutinesApi
    override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
        super.onFailure(webSocket, t, response)
        GlobalScope.launch {
            socketEventChannel.send("Error : ${t.message}")

        }
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}