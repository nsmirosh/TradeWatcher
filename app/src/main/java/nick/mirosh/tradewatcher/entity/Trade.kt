package nick.mirosh.tradewatcher.entity

import com.squareup.moshi.Json

data class Trade(
    @Json(name = "s")
    val symbol: String,
    @Json(name = "p")
    val price: Double,
    @Json(name = "t")
    val time: Long,
    @Json(name = "v")
    val volume: Double
)
