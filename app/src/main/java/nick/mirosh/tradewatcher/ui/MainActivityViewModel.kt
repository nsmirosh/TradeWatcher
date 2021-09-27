package nick.mirosh.tradewatcher.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import nick.mirosh.tradewatcher.entity.TradeView
import nick.mirosh.tradewatcher.repository.MainRepository

class MainActivityViewModel(val mainRepo: MainRepository) : ViewModel() {

    private val tag = MainActivityViewModel::class.java.simpleName

    private val _tradesUI = MutableStateFlow(listOf<TradeView>())
    val tradesUI: StateFlow<List<TradeView>> = _tradesUI

    private fun getTrades() {
        viewModelScope.launch {
            mainRepo
                .getTrades()
                .filterNot { it.isEmpty() }
                .collect {
                    _tradesUI.value = it.map {it.toTradeView()}
                }
        }
    }

    fun search(text: String) {
       mainRepo.getUpdates(text)
    }
}