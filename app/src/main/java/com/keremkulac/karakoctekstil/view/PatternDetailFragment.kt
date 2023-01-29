package com.keremkulac.karakoctekstil.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.keremkulac.karakoctekstil.R
import com.keremkulac.karakoctekstil.databinding.FragmentPatternDetailBinding
import com.keremkulac.karakoctekstil.model.Pattern
import com.keremkulac.karakoctekstil.util.*
import com.keremkulac.karakoctekstil.viewmodel.PatternDetailViewModel


class PatternDetailFragment : Fragment() {

    private lateinit var binding : FragmentPatternDetailBinding
    private lateinit var viewModel : PatternDetailViewModel
    private  var arg: Pattern? = null
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var floatingActionButton: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_pattern_detail,container,false)
        binding.patternDetailObject = this
        floatingActionButton = requireActivity().findViewById(R.id.fab)
        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        getPattern()
        bottomNavigationViewVisible()
        return binding.root
    }
    private fun bottomNavigationViewVisible(){
        bottomNavigationView.visibility = View.VISIBLE
        floatingActionButton.visibility = View.VISIBLE
    }
    private fun bottomNavigationViewGone(){
        floatingActionButton.visibility = View.GONE
        bottomNavigationView.visibility = View.GONE
    }

    private fun getPattern(){
        arg = arguments?.getParcelable<Pattern>("pattern")
        binding.detailPatternName.setText(arg?.name.toString())
        binding.detailPatternHeight.setText(arg?.height.toString())
        binding.detailPatternWidth.setText(arg?.width.toString())
        binding.detailPatternHit.setText(arg?.hit.toString())
        binding.detailPatternImageView.downloadFromUrl(arg?.pattern_url.toString(),
            placeHolderProgressBar(binding.root.context))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Desen Detayları"
        viewModel = ViewModelProviders.of(this).get(PatternDetailViewModel::class.java)
    }

    fun update(){
        val name = binding.detailPatternName.text.toString()
        val height =binding.detailPatternHeight.text.toString()
        val width = binding.detailPatternWidth.text.toString()
        val hit = binding.detailPatternHit.text.toString()
        val url = arg!!.pattern_url.toString()
        if(name.equals("") || height.equals("")|| width.equals("")|| hit.equals(""))  {
            Toast.makeText(binding.root.context,"Lütfen tüm bilgileri eksiksiz giriniz", Toast.LENGTH_SHORT).show()
        }else{
            val pattern = Pattern(name,width,height,hit,url)
            viewModel.updatePatternFromFirebase(arg!!.name.toString(),url,pattern,requireContext())
            refresh(pattern)
        }
    }

    private fun refresh(pattern: Pattern){
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        val args = Bundle()
        val patternDetailFragment = PatternDetailFragment()
        args.putParcelable("pattern",pattern)
        patternDetailFragment.setArguments(args)
        fragmentTransaction.replace(R.id.mainFrameLayout,patternDetailFragment)
        fragmentTransaction.commit()
    }

    fun patternImageFullScreen(){
        bottomNavigationViewGone()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        val args = Bundle()
        val fullScreenFragment = FullScreenFragment()
        args.putParcelable("pattern",arg)
        fullScreenFragment.setArguments(args)
        fragmentTransaction.replace(R.id.mainFrameLayout,fullScreenFragment)
        fragmentTransaction.commit()

    }

}