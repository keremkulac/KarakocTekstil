package com.keremkulac.karakoctekstil.viewmodel


import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.keremkulac.karakoctekstil.model.Pattern
import com.keremkulac.karakoctekstil.util.customProgressDialog
import com.keremkulac.karakoctekstil.util.replaceFragment
import com.keremkulac.karakoctekstil.view.PatternFragment
import java.util.*


class AddPatternViewModel(application: Application) : BaseViewModel(application) {
    private var isHaveSamePattern : Boolean? = false
    private var firestore: FirebaseFirestore
    private var firebaseDatabase : FirebaseDatabase
    private var storageReference = Firebase.storage.reference
    private lateinit var uuid : UUID
    private lateinit var pictureName : String
    private val EMPTY_PATTERN_IMAGE_URL= "https://firebasestorage.googleapis.com/v0/b/karakoctextile.appspot.com/o/images%2F8694a2f3-c01c-4ef6-8704-66a504f5db52.jpg?alt=media&token=e1c04430-3519-4093-b904-1368a7ab2a2e"
    private val BASE_PATTERN_IMAGE_URI= Uri.parse("android.resource://com.keremkulac.karakoctekstil/drawable/empty_pattern_image")


    init {
        firestore = FirebaseFirestore.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }


    fun firebaseSavePattern(pattern: Pattern, imageData: Uri?,context: Context,activity : FragmentActivity) {
      //  checkPattern(pattern.name.toString())
        if(pattern.name.toString().equals("") || pattern.height.toString().equals("") ||
            pattern.width.toString().equals("") || pattern.hit.toString().equals("") || imageData == null){
            Toast.makeText(context,"Lütfen tüm alanları doldurup tekrar deneyiniz",Toast.LENGTH_SHORT).show()
        }else{
            uuid = UUID.randomUUID()
            pictureName = "images/${uuid}.jpg"
            if(imageData == BASE_PATTERN_IMAGE_URI){
                hashPattern(pattern,EMPTY_PATTERN_IMAGE_URL,context,activity)
            }else {
                storageReference.child(pictureName).putFile(imageData!!).addOnSuccessListener {
                    val pictureReference = FirebaseStorage.getInstance().getReference(pictureName)
                    pictureReference.downloadUrl.addOnSuccessListener {
                        var pictureUrl = it.toString()
                        hashPattern(pattern, pictureUrl, context, activity)
                    }
                }
            }
        }

    }

    private fun hashPattern(pattern: Pattern,url : String,context: Context,activity : FragmentActivity){
        val progressDialog = customProgressDialog(context,"Desen kaydediliyor...")
        progressDialog.show()
        val hmPattern = HashMap<String, Any>()
        hmPattern["patternName"] = pattern.name.toString()
        hmPattern["patternWidth"] = pattern.width.toString()
        hmPattern["patternHeight"] = pattern.height.toString()
        hmPattern["patternHit"] =  pattern.hit.toString()
        hmPattern["patternURL"] = url
        firestore.collection("Patterns").document(pattern.name.toString())
            .set(hmPattern)
            .addOnSuccessListener {
                if(progressDialog.isShowing){
                    progressDialog.dismiss()
                }
                replaceFragment(PatternFragment(),activity.supportFragmentManager)
            }
    }
    private fun checkPattern(patternName :String){
        firestore.collection("Patterns")
           .get()
           .addOnSuccessListener {
               for(document in it){
                    if(patternName == document.data.get("patternName")){
                        isHaveSamePattern = true
                    }
               }
           }.addOnFailureListener {
                Log.d("TAG", it.toString())
            }
    }


}




