package nick.mirosh.tradewatcher

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import nick.mirosh.tradewatcher.ui.TradeAdapter
import tradewatcher.R
import tradewatcher.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    val mainActivityViewModel: MainActivityViewModel by viewModels()

    var binding: ActivityMainBinding? = null

    lateinit var listAdapter: TradeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

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