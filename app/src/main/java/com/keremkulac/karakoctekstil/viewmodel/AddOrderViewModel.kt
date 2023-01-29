package com.keremkulac.karakoctekstil.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.keremkulac.karakoctekstil.R
import com.keremkulac.karakoctekstil.model.Order
import com.keremkulac.karakoctekstil.util.replaceFragment
import com.keremkulac.karakoctekstil.view.OrderFragment
import java.util.HashMap

class AddOrderViewModel(application: Application) : BaseViewModel(application) {

    fun createSpinner(array: ArrayList<String>, act: AutoCompleteTextView, context: Context) {
        if(act != null){
            val adapter = ArrayAdapter(context, R.layout.spinner_view,array)
            act.setAdapter(adapter)
            act.setOnItemClickListener { adapterView, view, i, l ->
                Log.d("RAY",act.text.toString())
            }
        }
    }

    fun saveOrderFromFirebase(order : Order,fragmentActivity: FragmentActivity){
        var firestore = FirebaseFirestore.getInstance()
        val hmOrder = HashMap<String, Any>()
        hmOrder["orderPatternName"] = order.patternName
        hmOrder["orderClothType"] = order.clothType
        hmOrder["orderSeries"] = order.series
        hmOrder["orderPiece"] =  order.piece.toString()
        hmOrder["date"] =  order.date
        hmOrder["orderStatus"] = order.status
        firestore.collection("Orders").document(order.patternName)
            .set(hmOrder)
            .addOnSuccessListener {
                Toast.makeText(fragmentActivity.applicationContext,"Siparişiniz eklendi",Toast.LENGTH_SHORT).show()
                replaceFragment(OrderFragment(),fragmentActivity.supportFragmentManager)
            }
    }

}