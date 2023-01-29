package com.keremkulac.karakoctekstil.viewmodel

import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.keremkulac.karakoctekstil.R
import com.keremkulac.karakoctekstil.model.*
import com.keremkulac.karakoctekstil.service.CurrencyAPIService
import com.keremkulac.karakoctekstil.util.customProgressDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.HashMap

class CostViewModel : ViewModel() {
    private val currencyAPIService = CurrencyAPIService()
    private val disposable = CompositeDisposable()
    val patternNames = MutableLiveData<ArrayList<String>>()
    private  var firebaseFirestore : FirebaseFirestore

    init {
        firebaseFirestore = FirebaseFirestore.getInstance()
        getPatternNamesFromFirebase()
    }

    fun createSpinner(array: ArrayList<String>, act: AutoCompleteTextView, context: Context) {
        if(act != null){
            val adapter = ArrayAdapter(context, R.layout.spinner_view,array)
            act.setAdapter(adapter)
            act.setOnItemClickListener { adapterView, view, i, l ->
                Log.d("RAY",act.text.toString())
            }
        }
    }

    fun calculateCost(cost : Cost,exchangeRate: String,context: Context) : String{
        val progressDialog = customProgressDialog(context,"Fiyat hesaplanıyorr...")
        progressDialog.show()
        val emptyTerikoton = "2.700"
        var strip = 160.toDouble() / cost.cardWidth!!.toDouble()
        if(strip*cost.cardWidth.toDouble() > 150){
            strip--
        }
        val stripCardmeter = cost.cardMeter!!.toDouble() * strip
        Log.d("stripCardmeter",stripCardmeter.toString())
        val terikotonCost = String.format("%.2f",(cost.clothMeterPrice!!.toDouble() * exchangeRate.toDouble() * 10*2)/stripCardmeter)
        Log.d("terikotonCost",terikotonCost.toString())
        val yarnCost = String.format("%.2f", cost.yarnWeightPrice!!.toDouble()*exchangeRate.toDouble()*1.08)
        val yarnCost2 = String.format("%.2f",(1700.toDouble()/1920))
        Log.d("yarnCost",yarnCost.toString())
        Log.d("yarnCost2",yarnCost2.toString())
        val total = String.format("%.2f",((yarnCost2.toDouble() + cost.workmanship!!.toDouble() + cost.profit!!.toDouble()+ terikotonCost.toDouble()) / (cost.cardMeter.toDouble()*2)*100)/9)
        Log.d("total2",total.toString())
        Log.d("SERİES",cost.series)
        Log.d("CLOTH",cost.clothType)
        if(progressDialog.isShowing){
            progressDialog.dismiss()
        }
        return total

    }

    fun calculateTerikotonCost(cardMeter : String, cardWidth : String, clothMeterPrice : String, yarnPrice : String, workmanship : String, profit : String,exchangeRate : String) : String {
        val emptyTerikoton = "2.750"
        var strip = 160.toDouble() / cardWidth.toDouble()
        if(strip*cardWidth.toDouble() > 150){
            strip--
        }
        val stripCardmeter = cardMeter.toDouble() * strip
        Log.d("stripCardmeter",stripCardmeter.toString())
        val terikotonCost = String.format("%.2f",(clothMeterPrice.toDouble() * exchangeRate.toDouble() * 10*2)/stripCardmeter)
        Log.d("terikotonCost",terikotonCost.toString())
        val yarnCost = String.format("%.2f", yarnPrice.toDouble()*exchangeRate.toDouble()*1.08)
        val yarnCost2 = String.format("%.2f",(1700.toDouble()/1920))
        Log.d("yarnCost",yarnCost.toString())
        Log.d("yarnCost2",yarnCost2.toString())
        val total = String.format("%.2f",((yarnCost2.toDouble() + workmanship.toDouble() + profit.toDouble()+ terikotonCost.toDouble()) / (cardMeter.toDouble()*2)*100)/9)
        Log.d("total2",total.toString())
       return total
    }

    fun clear(array: ArrayList<TextInputEditText>){
        for(i in array){
            i.setText("")
        }
    }



    fun getDataFromCurrencyAPI(){
        disposable.add(
            currencyAPIService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<ExchangeRateList>>(){
                    override fun onSuccess(t: List<ExchangeRateList>) {
                        Log.d("LOG","t[0].usd.toString()")
                       // Log.d("LOG",t[1].name.toString())

                    }
                    override fun onError(e: Throwable) {
                        Log.d("LOG","Döviz kuru alınamadı")
                    }
                })
        )

    }

    fun saveCostToFirebase(result : String,cost : Cost,date : String) {
        val hmCost = HashMap<String, Any>()
        hmCost["patternName"] = cost.patternName
        hmCost["clothType"] = cost.clothType
        hmCost["series"] = cost.series
        hmCost["cardMeter"] = cost.cardMeter.toString()
        hmCost["cardWidth"] = cost.cardWidth.toString()
        hmCost["clothMeterPrice"] = cost.clothMeterPrice.toString()
        hmCost["yarnWeightPrice"] = cost.yarnWeightPrice.toString()
        hmCost["workmanship"] = cost.workmanship.toString()
        hmCost["profit"] = cost.profit.toString()
        hmCost["price"] = result
        hmCost["date"] = date
        firebaseFirestore.collection("Costs").document(cost.patternName)
            .set(hmCost)
            .addOnSuccessListener {
                Log.d("TAG","EKLENDİ")
            }


    }

    private fun getPatternNamesFromFirebase(){
        val list = ArrayList<String>()
        firebaseFirestore.collection("Patterns")
            .get()
            .addOnSuccessListener {document->
                for(firebaseData in document){
                    list.add(firebaseData.data.getValue("patternName").toString())
                }
                getNames(list)
            }
            .addOnFailureListener{
                it.printStackTrace()
              //  Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
    }

    fun getNames(list : ArrayList<String>){
        patternNames.value=list
    }


}