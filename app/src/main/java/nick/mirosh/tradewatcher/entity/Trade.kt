package nick.mirosh.tradewatcher.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Trade(
    @Json(name = "s")
    val symbol: String? = null,
    @Json(name = "p")
    val price: Double? = null,
    @Json(name = "t")
    val time: Long? = null,
    @Json(name = "v")
    val volume: Double? = null,
) {


    fun toTradeView(): TradeView {
        return TradeView(symbol.orEmpty(), price ?: 0.0, time ?: 0L )
    }
}
