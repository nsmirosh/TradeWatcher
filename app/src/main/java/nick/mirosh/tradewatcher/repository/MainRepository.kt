package nick.mirosh.tradewatcher.repository

import kotlinx.coroutines.flow.StateFlow
import nick.mirosh.tradewatcher.entity.Trade

interface MainRepository {

    fun getTrades(): StateFlow<List<Trade>>


    fun getUpdates(stockName: String)
}