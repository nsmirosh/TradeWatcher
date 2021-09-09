package nick.mirosh.tradewatcher.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import nick.mirosh.tradewatcher.entity.TradeView
import tradewatcher.R

class TradeAdapter :
    RecyclerView.Adapter<TradeAdapter.ViewHolder>() {

    var trades = mutableListOf<TradeView>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.stock_name)
        val price = itemView.findViewById<TextView>(R.id.price)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val contactView =
            LayoutInflater.from(parent.context).inflate(R.layout.row_trade, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = with(holder) {
        trades[position].let {
            name.text = it.symbol
            price.text = it.price.toString()
        }
    }


    //TODO use diffUtil here - https://medium.com/tech-carnot/making-recyclerview-efficient-using-list-adapter-with-diffutil-4a06d4583ea4
    @SuppressLint("NotifyDataSetChanged")
    fun setData(scooters: List<TradeView>) {
        this.trades.clear()
        this.trades.addAll(scooters)
        notifyDataSetChanged()
    }

    override fun getItemCount() = trades.size

}