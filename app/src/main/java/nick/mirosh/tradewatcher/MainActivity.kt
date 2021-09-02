package nick.mirosh.tradewatcher

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import tradewatcher.BuildConfig
import tradewatcher.R



class MainActivity : AppCompatActivity() {
    private var output: TextView? = null
    private var search: EditText? = null
    private lateinit var webSocket: WebSocket
    private lateinit var echoWebSocketListener: EchoWebSocketListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val start = findViewById<View>(R.id.start) as Button
        output = findViewById<View>(R.id.output) as TextView
        search = findViewById<View>(R.id.search_et) as EditText

        establishConnection()
        start.setOnClickListener { search(search!!.text.toString()) }
    }

    suspend fun onUpdate(newText: String) {
        runOnUiThread {
            val previousMessages = output!!.text
            output!!.text = "$previousMessages \n\n\n $newText"
        }
    }

    private fun establishConnection() {
        val client = OkHttpClient()
        val request = Request.Builder().url("${BuildConfig.WEBSOCKET_URL}?token=${BuildConfig.API_KEY}").build()
        echoWebSocketListener = EchoWebSocketListener()
        webSocket = client.newWebSocket(request, echoWebSocketListener)
        client.dispatcher.executorService.shutdown()
        GlobalScope.launch {
            echoWebSocketListener.tickFlow.collect {
                onUpdate(it)
            }
            /*echoWebSocketListener.socketEventChannel.consumeEach {
                onUpdate(it)
            }*/
        }
    }

    private fun search(text: String) {
        output!!.text = "Listening..."
        webSocket.send("{\"type\":\"subscribe\",\"symbol\":\"${text.uppercase()}\"}")
    }
}