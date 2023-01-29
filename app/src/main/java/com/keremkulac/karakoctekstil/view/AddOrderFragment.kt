package com.keremkulac.karakoctekstil.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.keremkulac.karakoctekstil.R
import com.keremkulac.karakoctekstil.databinding.FragmentAddOrderBinding
import com.keremkulac.karakoctekstil.model.Order
import com.keremkulac.karakoctekstil.util.replaceFragment
import com.keremkulac.karakoctekstil.viewmodel.AddOrderViewModel
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class AddOrderFragment : Fragment() {

    private lateinit var binding : FragmentAddOrderBinding
    private lateinit var viewModel : AddOrderViewModel
    private lateinit var seriesArray : ArrayList<String>
    private lateinit var clothTypeArray : ArrayList<String>
    private lateinit var order: Order
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        seriesArray = resources.getStringArray(R.array.seriesArray).toCollection(ArrayList())
        clothTypeArray = resources.getStringArray(R.array.typesOfClothArray).toCollection(ArrayList())

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_order,container,false)
        binding.orderObject = this
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddOrderViewModel::class.java)
        viewModel.createSpinner(seriesArray,binding.orderSpinnerSeries,view.context)
        viewModel.createSpinner(clothTypeArray,binding.orderSpinnerClothType,view.context)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveOrderDatabase(){
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            val date = ZonedDateTime.now(ZoneId.of("Asia/Istanbul")).toLocalDateTime().format(formatter)
            val patternName = binding.addOrderName.text.toString().uppercase().trim()
            val stripCount = binding.addOrderStripCount.text.toString()
            val orderClothType = binding.orderSpinnerClothType.text.toString()
            val orderSeries = binding.orderSpinnerSeries.text.toString()
            val orderStatus = "ongoing"
            if(patternName.equals("") || stripCount.equals("") || orderClothType.equals("") || orderSeries.equals("")){
                Toast.makeText(requireContext(),"Lütfen tüm bilgileri eksiksiz giriniz", Toast.LENGTH_SHORT).show()
            }else{
                order = Order(patternName,stripCount,orderClothType,orderSeries,date,orderStatus)
                viewModel.saveOrderFromFirebase(order,requireActivity())
                replaceFragment(OrderFragment(),requireActivity().supportFragmentManager)
            }
    }

}