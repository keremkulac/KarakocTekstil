package com.keremkulac.karakoctekstil.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.keremkulac.karakoctekstil.R
import com.keremkulac.karakoctekstil.databinding.FragmentFullScreenBinding
import com.keremkulac.karakoctekstil.model.Pattern


class FullScreenFragment : Fragment() {
    private lateinit var binding :FragmentFullScreenBinding
    private var pattern : Pattern? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View { // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_full_screen,container,false)
        binding.fullScreenFragmentObject = this
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        getPatternUrl()
        full()
        return binding.root
    }
    private fun getPatternUrl(){
        pattern = arguments?.getParcelable<Pattern>("pattern")
    }

    private fun full(){
        Glide.with(requireContext()).load(pattern?.pattern_url.toString()).into(binding.imageViewFullScreen)
    }
    fun exit(){
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        val args = Bundle()
        val patternDetailFragment = PatternDetailFragment()
        args.putParcelable("pattern",pattern)
        patternDetailFragment.setArguments(args)
        fragmentTransaction.replace(R.id.mainFrameLayout,patternDetailFragment)
        fragmentTransaction.commit()
    }
    fun zoom(){

    }
}