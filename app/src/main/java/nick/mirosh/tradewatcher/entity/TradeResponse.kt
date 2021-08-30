package nick.mirosh.tradewatcher.entity

data class TradeResponse(
    val data: List<Trade>,
    val type: String
)