package nick.mirosh.tradewatcher.entity

data class TradeView(
    val symbol: String = "",
    val price: Double = 0.0,
    val time: Long = 0L,
) {
    companion object {
        val empty = TradeView("", 0.0, 0L)
    }
}
