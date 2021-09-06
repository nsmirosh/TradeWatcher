package nick.mirosh.tradewatcher.entity

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class TradeResponse(
    val data: List<Trade>? = null,
    val type: String? = null,
)