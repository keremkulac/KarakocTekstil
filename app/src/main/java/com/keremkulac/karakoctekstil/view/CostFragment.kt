package com.keremkulac.karakoctekstil.view

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.keremkulac.karakoctekstil.R
import com.keremkulac.karakoctekstil.databinding.FragmentCostBinding
import com.keremkulac.karakoctekstil.model.Cost
import com.keremkulac.karakoctekstil.util.replaceFragment
import com.keremkulac.karakoctekstil.viewmodel.CostViewModel
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class CostFragment : Fragment() {
    private lateinit var binding : FragmentCostBinding
    private lateinit var viewModel : CostViewModel
    private lateinit var seriesArray : ArrayList<String>
    private lateinit var clothTypeArray : ArrayList<String>
    private lateinit var cost : Cost
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = CostViewModel()
        seriesArray = resources.getStringArray(R.array.seriesArray).toCollection(ArrayList())
        clothTypeArray = resources.getStringArray(R.array.typesOfClothArray).toCollection(ArrayList())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_cost,container,false)
        binding.costObject = this
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Fiyat Hesapla"
        viewModel.createSpinner(seriesArray,binding.costSpinnerSeries,view.context)
        viewModel.createSpinner(clothTypeArray,binding.costSpinnerClothType,view.context)
        observeLiveData(view)
        setSearchMenu()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getInputsAndCalculate()  {
        val cardWith = binding.costWidth.text.toString()
        val cardMeter =binding.costCardMeter.text.toString()
        val clothPrice = binding.costClothMeterPrice.text.toString()
        val yarnMeterPrice = binding.costYarnWeightPrice.text.toString()
        val workmanship = binding.costWorkmanship.text.toString()
        val profit = binding.costProfit.text.toString()
        val series = binding.costSpinnerSeries.text.toString()
        val clothType = binding.costSpinnerClothType.text.toString()
        val patternName = binding.costSpinnerPatternNames.text.toString()
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        val date = ZonedDateTime.now(ZoneId.of("Asia/Istanbul")).toLocalDateTime().format(formatter)
        if(cardWith.equals("") || cardMeter.equals("")|| clothPrice.equals("")|| yarnMeterPrice.equals("")||
            workmanship.equals("")|| profit.equals("")|| series.equals("")|| clothType.equals("") || patternName.equals("")){
            Toast.makeText(requireContext(),"Lütfen tüm bilgileri eksiksiz giriniz",Toast.LENGTH_SHORT).show()
        }else{
            cost = Cost(cardMeter,cardWith,clothPrice,yarnMeterPrice,workmanship,profit,series,clothType,patternName)
            val result = viewModel.calculateCost(cost,"18.6",requireContext())
            val builder = AlertDialog.Builder(binding.root.context)
            builder.setTitle("Fiyatı kaydetmek istiyor musunuz?")
            builder.setMessage("Hesaplanan fiyat: "+ result)
            builder.setPositiveButton("Evet") { dialog, which ->
                activity?.let {
                    viewModel.saveCostToFirebase(result,cost,date)
                    replaceFragment(CostFragment(),requireActivity().supportFragmentManager)
                    dialog.dismiss()
                }
            }
            builder.setNegativeButton("İptal") { dialog, which ->
                dialog.dismiss()
            }
            builder.show()
            }
        }

    private fun observeLiveData(view : View){
        viewModel.patternNames.observe(viewLifecycleOwner, Observer {list->
            viewModel.createSpinner(list,binding.costSpinnerPatternNames,view.context)
        })
    }

    fun clear(){
        replaceFragment(CostFragment(),requireActivity().supportFragmentManager)
    }

    private fun setSearchMenu(){
        val menuHost : MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.price_list_menu,menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.actionViewCosts -> {
                        replaceFragment(PatternPricesFragment(),requireActivity().supportFragmentManager)
                        true
                    }
                    else -> false
                }
            }

        },viewLifecycleOwner,Lifecycle.State.RESUMED)
    }

}