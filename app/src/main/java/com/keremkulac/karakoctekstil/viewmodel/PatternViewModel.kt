package com.keremkulac.karakoctekstil.viewmodel

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.keremkulac.karakoctekstil.adapter.PatternAdapter
import com.keremkulac.karakoctekstil.model.Pattern
import com.keremkulac.karakoctekstil.util.replaceFragment
import com.keremkulac.karakoctekstil.view.PatternFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class PatternViewModel(application: Application) : BaseViewModel(application) {
    val patterns = MutableLiveData<List<Pattern>>()
    val patternError = MutableLiveData<Boolean>()
    val patternLoading =  MutableLiveData<Boolean>()
    private var initialPatternList = listOf<Pattern>()
    private var isSearchStarting = true
    val EMPTY_PATTERN_IMAGE_URL= "https://firebasestorage.googleapis.com/v0/b/karakoctextile.appspot.com/o/images%2F8694a2f3-c01c-4ef6-8704-66a504f5db52.jpg?alt=media&token=e1c04430-3519-4093-b904-1368a7ab2a2e"

    init {
        getPatternsFromFirebase()
    }


    fun getPatternsFromFirebase(){
        val list1 = ArrayList<Pattern>()
        var pattern : Pattern
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("Patterns").get().addOnSuccessListener { document ->
            if (document != null) {
                for(firebaseData in document){
                    pattern = Pattern(
                        firebaseData.data.getValue("patternName").toString(),
                        firebaseData.data.getValue("patternWidth").toString(),
                        firebaseData.data.getValue("patternHeight").toString(),
                        firebaseData.data.getValue("patternHit").toString(),
                        firebaseData.data.getValue("patternURL").toString()
                    )
                    list1.add(pattern)
                }
                showPatterns(list1)
            }
            else {
                Log.d("TAG","başarısız")
            }
        }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
    }

    fun searchPatternList(query: String?){
        query.let {query->
            val listToSearch = if(isSearchStarting){
            patterns.value
            }else{
                initialPatternList
            }
                viewModelScope.launch(Dispatchers.Default) {
                    if (query != null) {
                        if(query.isEmpty()){
                            patterns.postValue(initialPatternList)
                            return@launch
                        }
                    }
                    if(isSearchStarting){
                        initialPatternList = patterns.value!!
                        isSearchStarting = false
                    }
                    val result = listToSearch!!.filter {
                        query?.let { it1 -> it.name!!.contains(it1.trim(),ignoreCase = true) }!!
                    }
                    patterns.postValue(result)
                }
            }

    }

     fun filter(text: String,patternList: ArrayList<Pattern>,adapter: PatternAdapter) {
        val filteredlist: ArrayList<Pattern> = ArrayList()

        for (item in patternList) {
            if (item.name!!.toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
           // Toast.makeText(context, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            adapter.filterList(filteredlist)
        }
    }


    private fun showPatterns(patternList : List<Pattern>){
        patterns.value = patternList
        patternError.value = false
        patternLoading.value = false
    }

    fun deletePattern(fragmentActivity: FragmentActivity, patternName : String, url : String, context: Context){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Uyarı")
        builder.setMessage("$patternName deseni silmek istiyor musunuz?")
        builder.setPositiveButton("Evet") { dialog, which ->
            deletePatternFromFirebase(patternName,context)
            deletePatternImageFromFirebase(url)
            Toast.makeText(context, patternName+" isimli desen başarıyla silindi!", Toast.LENGTH_SHORT).show()
            fragmentActivity?.let {
                replaceFragment(PatternFragment(), it.supportFragmentManager)
                dialog.dismiss()
            }
        }
        builder.setNegativeButton("İptal") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun deletePatternFromFirebase(patternName : String,context: Context){
        var firestore = FirebaseFirestore.getInstance()
        firestore.collection("Patterns").document(patternName)
            .delete()
            .addOnSuccessListener { Log.d("TAG","SUCCESSFUL") }
            .addOnFailureListener { Log.d("TAG","ERROR") }
    }
    private fun deletePatternImageFromFirebase(url : String){
        if(url != EMPTY_PATTERN_IMAGE_URL){
            val firebaseStorage = FirebaseStorage.getInstance()
            val storageReference = firebaseStorage.getReferenceFromUrl(url)
            storageReference.delete().addOnSuccessListener {
                // Log.e("TAG", "#deleted")
            }
        }
    }


}