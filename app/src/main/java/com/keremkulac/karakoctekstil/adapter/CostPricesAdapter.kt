package com.keremkulac.karakoctekstil.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.keremkulac.karakoctekstil.R
import kotlinx.android.synthetic.main.item_cost.view.*

class CostPricesAdapter(

    var costPricesList: ArrayList<HashMap<String,Any>>) : RecyclerView.Adapter<CostPricesAdapter.CostPricesViewHolder>(){
    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CostPricesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_cost,parent,false)
        return CostPricesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CostPricesViewHolder, position: Int) {
        holder.itemView.patternName.text = costPricesList[position].get("patternName").toString()
        holder.itemView.clothType.text = costPricesList[position].get("clothType").toString()
        holder.itemView.series.text = costPricesList[position].get("series").toString()
        holder.itemView.clothMeterPrice.text = costPricesList[position].get("clothMeterPrice").toString()
        holder.itemView.yarnWeightPrice.text = costPricesList[position].get("yarnWeightPrice").toString()
        holder.itemView.profit.text = costPricesList[position].get("profit").toString()
        holder.itemView.price.text = costPricesList[position].get("price").toString()+" TL"
        holder.itemView.date.text = costPricesList[position].get("date").toString()
        setAnimation(holder.itemView,position,holder.itemView.context)
    }


    override fun getItemCount(): Int {
        return costPricesList.size
    }

    fun filterList(filterlist: ArrayList<HashMap<String,Any>>) {
        costPricesList = filterlist
        notifyDataSetChanged()
    }

    fun updateCostPricesList(newOrderList : List<HashMap<String,Any>>){
        costPricesList.clear()
        costPricesList.addAll(newOrderList)
        notifyDataSetChanged()
    }
    class CostPricesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    private fun setAnimation(viewToAnimate : View, position: Int, context : Context){
        if(position > lastPosition){
            val slideIn = AnimationUtils.loadAnimation(context,android.R.anim.slide_in_left)
            //     val slideIn = AnimationUtils.loadAnimation(context,R.anim.rcy_anim)
            viewToAnimate.startAnimation(slideIn)
            lastPosition = position
        }

    }
}