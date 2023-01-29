package com.keremkulac.karakoctekstil.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.keremkulac.karakoctekstil.adapter.OrderAdapter
import com.keremkulac.karakoctekstil.model.Order

class FinishedOrderViewModel(application: Application) : BaseViewModel(application) {

    val orders = MutableLiveData<List<Order>>()
    val orderError = MutableLiveData<Boolean>()
    val orderLoading =  MutableLiveData<Boolean>()

    init {
        getOrdersFromFirebase()
    }

    fun getOrdersFromFirebase(){
        val list = ArrayList<Order>()
        var order : Order
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("Orders").get().addOnSuccessListener { document ->
            if (document != null) {
                for(firebaseData in document){
                    order = Order(
                        firebaseData.data.getValue("orderPatternName").toString(),
                        firebaseData.data.getValue("orderPiece").toString(),
                        firebaseData.data.getValue("orderClothType").toString(),
                        firebaseData.data.getValue("orderSeries").toString(),
                        firebaseData.data.getValue("date").toString() ,
                        firebaseData.data.getValue("orderStatus").toString()
                    )
                    if(firebaseData.data.getValue("orderStatus").toString().equals("finished")){
                        list.add(order)
                    }
                }
                showOrders(list)
            }
            else {
                Log.d("TAG","başarısız")
            }
        }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }

    }
    private fun showOrders(orderList : List<Order>){
        orders.value = orderList
        orderError.value = false
        orderLoading.value = false
    }

    fun filter(text: String, orderList : ArrayList<Order>, context: Context, orderAdapter : OrderAdapter) {
        val filteredlist: ArrayList<Order> = ArrayList()

        for (item in orderList) {
            if (item.patternName.toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            // Toast.makeText(context, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            orderAdapter.filterList(filteredlist)
        }
    }
}