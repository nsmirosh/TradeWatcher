package nick.mirosh.tradewatcher.entity

data class Trade(
    val s: String, // symbol
    val p: Double, // last price
    val t: Int,
    val v: Double //volume
)
