package com.keremkulac.karakoctekstil.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.keremkulac.karakoctekstil.view.*

class TablayoutAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private val fragmentTitleList = mutableListOf("Sipariş ekle", "Desen ekle")

    override fun getItem(position:Int): Fragment{

        when(position){
            0-> return AddOrderFragment()
            1-> return AddPatternFragment()
            else -> return AddOrderFragment()
        }
    }

    override fun getPageTitle(position: Int):String{

        return fragmentTitleList.get(position)
    }
    override fun getCount(): Int = 2



}