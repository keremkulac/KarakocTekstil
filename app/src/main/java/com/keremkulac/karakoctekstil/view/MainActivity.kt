package com.keremkulac.karakoctekstil.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.keremkulac.karakoctekstil.R
import com.keremkulac.karakoctekstil.util.replaceFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView.background=null
        bottomNavigationView.menu.getItem(2).isEnabled=false
        var fragmentManager = supportFragmentManager
        bottomNavigationSelect(fragmentManager)
        replaceFragment(PatternFragment(),fragmentManager)
        fab.setOnClickListener {
            bottomNavigationView.menu.getItem(0).isChecked = false
            replaceFragment(AddFragment(),fragmentManager)
        }
    }

    private fun bottomNavigationSelect(fragmentManager: FragmentManager){
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navPattern -> replaceFragment(PatternFragment(),fragmentManager)
                R.id.navCost -> replaceFragment(CostFragment(),fragmentManager)
                R.id.navOrder -> replaceFragment(OrderFragment(),fragmentManager)
                R.id.navExit-> finishAndRemoveTask()
                else -> {
                }
            }
            true
        }
    }

}


