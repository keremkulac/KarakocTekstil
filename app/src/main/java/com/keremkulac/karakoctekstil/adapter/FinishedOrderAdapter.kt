package com.keremkulac.karakoctekstil.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.keremkulac.karakoctekstil.R
import com.keremkulac.karakoctekstil.databinding.ItemFinishedOrderBinding
import com.keremkulac.karakoctekstil.model.Order
import kotlinx.android.synthetic.main.item_finished_order.view.*

class FinishedOrderAdapter(
    var orderList: ArrayList<Order>) : RecyclerView.Adapter<FinishedOrderAdapter.OrderViewHolder>(){
    private var lastPosition = -1


    class OrderViewHolder(val binding: ItemFinishedOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order : Order){
            binding.order = order
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinishedOrderAdapter.OrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemFinishedOrderBinding>(inflater,R.layout.item_finished_order,parent,false)
        return OrderViewHolder(view)

    }
    override fun onBindViewHolder(holder: FinishedOrderAdapter.OrderViewHolder, position: Int) {
        holder.apply {
            bind(orderList[position])
        }

        holder.itemView.orderStatus.text = "Bitti"
        holder.itemView.orderStatus.setTextColor(Color.parseColor("#FF0000"))

        setAnimation(holder.itemView,position,holder.itemView.context)
    }



    override fun getItemCount(): Int {
        return orderList.size
    }

    fun filterList(filterlist: ArrayList<Order>) {
        orderList = filterlist
        notifyDataSetChanged()
    }

    fun updateOrderList(newOrderList : List<Order>){
        orderList.clear()
        orderList.addAll(newOrderList)
        notifyDataSetChanged()
    }


    private fun setAnimation(viewToAnimate : View, position: Int, context : Context){
        if(position > lastPosition){
            val slideIn = AnimationUtils.loadAnimation(context,android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(slideIn)
            lastPosition = position
        }
    }


}