package nick.mirosh.tradewatcher

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tradewatcher.R

class MainActivity : AppCompatActivity() {
    private var output: TextView? = null
    private var search: EditText? = null


    val viewModel: MainActivityViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val start = findViewById<View>(R.id.start) as Button
        output = findViewById<View>(R.id.output) as TextView
        search = findViewById<View>(R.id.search_et) as EditText
        start.setOnClickListener { search(search!!.text.toString()) }
    }

    private fun startUpdatingTheUi() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.tradesUI.collect {
                val previousMessages = output!!.text
                output!!.text = "$previousMessages \n\n\n $it"
            }
        }
    }

    private fun search(text: String) {
        output!!.text = "Listening..."
        viewModel.search(text)
        startUpdatingTheUi()
    }
}