package com.keremkulac.karakoctekstil.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.keremkulac.karakoctekstil.view.FinishedOrderFragment
import com.keremkulac.karakoctekstil.view.OngoingOrderFragment

    class OrderTablayoutAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private val fragmentTitleList = mutableListOf("Devam eden siparişler", "Biten siparişler")

        override fun getItem(position:Int): Fragment{

            when(position){
                0-> return OngoingOrderFragment()
                1-> return FinishedOrderFragment()
                else -> return OngoingOrderFragment()
            }
        }

        override fun getPageTitle(position: Int):String{
            return fragmentTitleList.get(position)
        }
        override fun getCount(): Int = 2
        /*
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    OngoingOrderFragment()
                }
                1 -> FinishedOrderFragment()
                else -> {
                    return OngoingOrderFragment()
                }
            }
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence {
            return when (position) {
                0 -> "Devam eden siparişler"
                1 -> "Biten siparişler"
                else -> {
                    return "Devam eden siparişler"
                }
            }
        }

         */
    }