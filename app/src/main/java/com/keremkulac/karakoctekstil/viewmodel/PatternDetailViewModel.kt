package com.keremkulac.karakoctekstil.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.keremkulac.karakoctekstil.model.Pattern
import java.util.HashMap


class PatternDetailViewModel(application: Application) : BaseViewModel(application) {
    val EMPTY_PATTERN_IMAGE_URL= "https://firebasestorage.googleapis.com/v0/b/karakoctextile.appspot.com/o/images%2F8694a2f3-c01c-4ef6-8704-66a504f5db52.jpg?alt=media&token=e1c04430-3519-4093-b904-1368a7ab2a2e"
    fun deletePatternFromFirebase(patternName : String,context: Context){
        var firestore = FirebaseFirestore.getInstance()
        firestore.collection("Patterns").document(patternName)
            .delete()
            .addOnSuccessListener { Log.d("TAG","SUCCESSFUL") }
            .addOnFailureListener { Log.d("TAG","ERROR") }
    }
    fun deletePatternImageFromFirebase(url : String){
        if(url != EMPTY_PATTERN_IMAGE_URL){
            val firebaseStorage = FirebaseStorage.getInstance()
            val storageReference = firebaseStorage.getReferenceFromUrl(url)
            storageReference.delete().addOnSuccessListener {
               // Log.e("TAG", "#deleted")
            }
        }
    }
    fun updatePatternFromFirebase(patternName: String,url : String,pattern: Pattern,context: Context){
        deletePatternFromFirebase(patternName,context)
        // deletePatternImageFromFirebase(url)
        savePatternFromFirebase(pattern,url,context)
    }

     fun savePatternFromFirebase(pattern: Pattern,url: String,context: Context){
        var firestore = FirebaseFirestore.getInstance()
        val hmPattern = HashMap<String, Any>()
        hmPattern["patternName"] = pattern.name.toString()
        hmPattern["patternWidth"] = pattern.width.toString()
        hmPattern["patternHeight"] = pattern.height.toString()
        hmPattern["patternHit"] =  pattern.hit.toString()
        hmPattern["patternURL"] = url
        firestore.collection("Patterns").document(pattern.name.toString())
            .set(hmPattern)
            .addOnSuccessListener {
                Toast.makeText(context,"Başarıyla güncellendi",Toast.LENGTH_SHORT).show()
            }
    }


}