package com.keremkulac.karakoctekstil.viewmodel

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.keremkulac.karakoctekstil.adapter.OngoingOrderAdapter
import com.keremkulac.karakoctekstil.model.Order
import com.keremkulac.karakoctekstil.util.replaceFragment
import com.keremkulac.karakoctekstil.view.OrderFragment
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.HashMap

class OngoingOrderViewModel(application: Application) : BaseViewModel(application) {

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
                        firebaseData.data.getValue("remainderOrderPiece").toString(),
                        firebaseData.data.getValue("orderClothType").toString(),
                        firebaseData.data.getValue("orderSeries").toString(),
                        firebaseData.data.getValue("orderDate").toString() ,
                        firebaseData.data.getValue("orderEndDate").toString(),
                        firebaseData.data.getValue("orderStatus").toString(),
                        firebaseData.data.getValue("orderID").toString()
                    )
                    if(firebaseData.data.getValue("orderStatus").toString().equals("ongoing")){
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun decreasePiece(order : Order, context : Context, fragmentActivity : FragmentActivity){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Uyarı")
        builder.setMessage("${order.patternName} siparişine 2 kupon eklemek istiyor musunuz?")
        builder.setPositiveButton("Evet") { dialog, which ->
            fragmentActivity?.let {
                val hmOrder = HashMap<String, Any>()
                val newPiece = Integer.parseInt(order.remainderPiece)-2
                if(newPiece <= 0) {
                    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                    val date = ZonedDateTime.now(ZoneId.of("Asia/Istanbul")).toLocalDateTime().format(formatter)
                    hmOrder["orderStatus"] = "finished"
                    hmOrder["orderEndDate"] = date
                    Toast.makeText(fragmentActivity.applicationContext,"Siparişiniz bitti.",Toast.LENGTH_SHORT).show()
                    replaceFragment(OrderFragment(),fragmentActivity.supportFragmentManager)
                }else{
                    hmOrder["orderStatus"] = order.status
                    hmOrder["orderEndDate"] = order.orderEndDate
                }
                hmOrder["orderPatternName"] = order.patternName
                hmOrder["orderClothType"] = order.clothType
                hmOrder["orderSeries"] = order.series
                hmOrder["remainderOrderPiece"] = newPiece.toString()
                hmOrder["orderDate"] =  order.orderDate

                hmOrder["orderID"] = order.orderID
                hmOrder["orderPiece"] = order.piece.toString()
                val firestore = FirebaseFirestore.getInstance()
                firestore.collection("Orders").document(order.orderID)
                    .set(hmOrder)
                    .addOnSuccessListener {
                        Toast.makeText(fragmentActivity.applicationContext,"${order.patternName} siparişinize yapılan 2 kupon eklendi eklendi",Toast.LENGTH_SHORT).show()
                        replaceFragment(OrderFragment(),fragmentActivity.supportFragmentManager)
                    }


            }
        }
        builder.setNegativeButton("İptal") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun showOrders(orderList : List<Order>){
        orders.value = orderList
        orderError.value = false
        orderLoading.value = false
    }

    fun filter(text: String, orderList : ArrayList<Order>, context: Context, orderAdapter : OngoingOrderAdapter) {
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