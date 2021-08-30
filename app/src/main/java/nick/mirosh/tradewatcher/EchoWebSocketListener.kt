package nick.mirosh.tradewatcher

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class EchoWebSocketListener : WebSocketListener() {

    val socketEventChannel: Channel<String> = Channel(10)

    override fun onOpen(webSocket: WebSocket, response: Response) {
    }

    @DelicateCoroutinesApi
    override fun onMessage(webSocket: WebSocket, text: String) {
        GlobalScope.launch {
            socketEventChannel.send(text)
        }
    }

    @DelicateCoroutinesApi
    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        GlobalScope.launch {
            socketEventChannel.send(bytes.hex())
        }
    }

    @DelicateCoroutinesApi
    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        GlobalScope.launch {
            socketEventChannel.send("Closing : $code / $reason")
        }
    }

    @DelicateCoroutinesApi
    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        GlobalScope.launch {
            socketEventChannel.send("Error : ${t.message}")

        }
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}