package com.keremkulac.karakoctekstil.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.keremkulac.karakoctekstil.R
import com.keremkulac.karakoctekstil.databinding.ItemOngoingOrderBinding
import com.keremkulac.karakoctekstil.model.Order
import com.keremkulac.karakoctekstil.viewmodel.OngoingOrderViewModel
import kotlinx.android.synthetic.main.item_ongoing_order.view.*

class OngoingOrderAdapter(
    var context: Context,
    var fragmentActivity: FragmentActivity,
    var ongoingOrderViewModel: OngoingOrderViewModel,
    var orderList: ArrayList<Order>) : RecyclerView.Adapter<OngoingOrderAdapter.OrderViewHolder>(){
    private var lastPosition = -1


    class OrderViewHolder(val binding: ItemOngoingOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order : Order){
            binding.order = order
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemOngoingOrderBinding>(inflater,R.layout.item_ongoing_order,parent,false)
        return OrderViewHolder(view)

    }


    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.apply {
            bind(orderList[position])
        }
        holder.itemView.orderStatus.text= "Devam ediyor"
        holder.itemView.orderStatus.setTextColor(Color.parseColor("#59C639"))
        setAnimation(holder.itemView,position,holder.itemView.context)
        holder.itemView.decreaseTwoPiece.setOnClickListener{
            ongoingOrderViewModel.decreasePiece(orderList[position],context,fragmentActivity)
        }
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