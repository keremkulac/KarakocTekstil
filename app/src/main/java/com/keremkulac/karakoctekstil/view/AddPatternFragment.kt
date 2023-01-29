package com.keremkulac.karakoctekstil.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.keremkulac.karakoctekstil.R
import com.keremkulac.karakoctekstil.databinding.FragmentAddPatternBinding
import com.keremkulac.karakoctekstil.model.Pattern
import com.keremkulac.karakoctekstil.viewmodel.AddPatternViewModel


class AddPatternFragment : Fragment() {

    private lateinit var viewModel : AddPatternViewModel
    private lateinit var  pref : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor
    private lateinit var binding : FragmentAddPatternBinding
    private lateinit var baseUri : Uri
    private var imageData : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddPatternViewModel::class.java)
        sharedPrefSettings()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
       // binding = FragmentAddPatternBinding.inflate(inflater,container,false)
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_pattern,container,false)
        binding.addPatternObject = this

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       (activity as AppCompatActivity?)!!.supportActionBar!!.title = ""
       // (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        baseUri = Uri.parse("android.resource://com.keremkulac.karakoctekstil/drawable/empty_pattern_image")
        imageData = baseUri
    }


    private fun sharedPrefSettings(){
        pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        editor = pref.edit()
        editor.putBoolean("request",false)
        editor.commit()
    }

    fun patternCreate(){
        val pattern = Pattern(
            name=binding.addPatternName.text.toString().uppercase().trim(),
            width=binding.addPatternWidth.text.toString().trim(),
            height=binding.addPatternHeight.text.toString().trim(),
            hit=binding.addPatternHit.text.toString().trim(),
            pattern_url="").apply {
        }
        viewModel.firebaseSavePattern(pattern,imageData,requireContext(),requireActivity())
    }

    fun selectImage(){

        if(pref.getBoolean("request",false) == false ){
            if(checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                Snackbar.make(binding.root, "Galeriye ulaşmak için izin gerekli", Snackbar.LENGTH_INDEFINITE).setAction("İzin ver") {
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1) }.show()
                    editor.putBoolean("request",true)
                    editor.commit()
            }else{
                intentFromGallery()
            }
        }else{
            intentFromGallery()
        }

    }
    private fun intentFromGallery(){
        val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,2)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == 1){
            if(true){
                intentFromGallery()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
            imageData = data.data

        }


        try {
            if(imageData != null){
                if(Build.VERSION.SDK_INT >= 28){
                    Glide.with(requireContext()).load(imageData).into(binding.addPatternImageView)
                }else{
                    Glide.with(requireContext()).load(imageData).into(binding.addPatternImageView)
                }
            }else{
                val drawable = resources.getDrawable(R.drawable.empty_pattern_image)
                Glide.with(requireContext()).load(drawable).into(binding.addPatternImageView)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }






}