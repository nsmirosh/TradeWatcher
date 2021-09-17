package nick.mirosh.tradewatcher

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import nick.mirosh.tradewatcher.ui.TradeAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import tradewatcher.R
import tradewatcher.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
//    private var search: EditText? = null
//    private var tradesList: RecyclerView? = null

    val mainActivityViewModel: MainActivityViewModel by viewModel()

    var binding: ActivityMainBinding? = null

    lateinit var listAdapter: TradeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
/*        val start = findViewById<View>(R.id.start) as Button
        search = findViewById<View>(R.id.search_et) as EditText
        tradesList = findViewById(R.id.trades_rv)*/

        binding!!.start.setOnClickListener { performSearch() }
        setUpRecyclerList()
    }

    private fun setUpRecyclerList() =
        with(binding!!.tradesRv) {
            listAdapter = TradeAdapter()
            adapter = listAdapter
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        }

    private fun startUpdatingTheUi() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            mainActivityViewModel.tradesUI.collect {
                listAdapter.setData(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun performSearch() {
        mainActivityViewModel.search(binding!!.searchEt.text.toString())
        startUpdatingTheUi()
    }
}