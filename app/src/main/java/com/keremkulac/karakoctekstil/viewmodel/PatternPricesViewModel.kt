package com.keremkulac.karakoctekstil.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.keremkulac.karakoctekstil.adapter.CostPricesAdapter

class PatternPricesViewModel(application: Application) : BaseViewModel(application) {
    val costPrices = MutableLiveData<List<HashMap<String,Any>>>()
    private var firebaseFirestore : FirebaseFirestore
    init{
        firebaseFirestore = FirebaseFirestore.getInstance()
        getCostPricessFromFirebase()
    }

    fun getCostPricessFromFirebase(){
        val list = ArrayList<HashMap<String,Any>>()
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("Costs").get().addOnSuccessListener { document ->
            if (document != null) {
                for(firebaseData in document){
                    val hm = HashMap<String,Any>()
                    hm["patternName"]= firebaseData.data.getValue("patternName").toString()
                    hm["clothType"]= firebaseData.data.getValue("clothType").toString()
                    hm["series"]= firebaseData.data.getValue("series").toString()
                    hm["clothMeterPrice"]=firebaseData.data.getValue("clothMeterPrice").toString()
                    hm["yarnWeightPrice"]=firebaseData.data.getValue("yarnWeightPrice").toString()
                    hm["workmanship"]= firebaseData.data.getValue("workmanship").toString()
                    hm["profit"]= firebaseData.data.getValue("profit").toString()
                    hm["price"]= firebaseData.data.getValue("price").toString()
                    hm["date"]=firebaseData.data.getValue("date").toString()
                    list.add(hm)
                }
                getCostPrices(list)
            }
            else {
                Log.d("TAG","başarısız")
            }
        }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }

    }

    fun getCostPrices(hm : ArrayList<HashMap<String,Any>>){
        costPrices.value = hm
    }

    fun filter(text: String, list: ArrayList<HashMap<String,Any>>, adapter: CostPricesAdapter) {
        val filteredlist: ArrayList<HashMap<String,Any>> = ArrayList()

        for (item in list) {
            if (item.getValue("patternName").toString()!!.toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            // Toast.makeText(context, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            adapter.filterList(filteredlist)
        }
    }

}