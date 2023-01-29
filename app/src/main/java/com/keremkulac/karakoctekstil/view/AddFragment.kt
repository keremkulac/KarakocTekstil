package com.keremkulac.karakoctekstil.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.keremkulac.karakoctekstil.adapter.TablayoutAdapter
import com.keremkulac.karakoctekstil.databinding.FragmentAddBinding


class AddFragment : Fragment() {

    private lateinit var binding : FragmentAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddBinding.inflate(inflater,container,false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureTopNavigation()
    }

    private fun configureTopNavigation(){
        binding.viewPager.adapter = TablayoutAdapter(childFragmentManager)
        binding.viewPager.offscreenPageLimit = 2
        binding.tabLayout.setupWithViewPager(binding.viewPager)

    }

}