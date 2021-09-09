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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import nick.mirosh.tradewatcher.ui.TradeAdapter
import tradewatcher.R

class MainActivity : AppCompatActivity() {
    private var search: EditText? = null
    private var tradesList: RecyclerView? = null

    val viewModel: MainActivityViewModel by viewModels()

    lateinit var listAdapter: TradeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val start = findViewById<View>(R.id.start) as Button
        search = findViewById<View>(R.id.search_et) as EditText
        tradesList = findViewById(R.id.trades_rv)

        start.setOnClickListener { search(search!!.text.toString()) }
        setUpRecyclerList()
    }

    private fun setUpRecyclerList() =
        with(tradesList!!) {
            listAdapter = TradeAdapter()
            adapter = listAdapter
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        }

    private fun startUpdatingTheUi() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.tradesUI.collect {
                listAdapter.setData(it)
            }
        }
    }

    private fun search(text: String) {
        viewModel.search(text)
        startUpdatingTheUi()
    }
}