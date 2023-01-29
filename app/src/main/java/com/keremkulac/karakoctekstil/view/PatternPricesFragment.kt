package com.keremkulac.karakoctekstil.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremkulac.karakoctekstil.R
import com.keremkulac.karakoctekstil.adapter.CostPricesAdapter
import com.keremkulac.karakoctekstil.databinding.FragmentPatternPricesBinding
import com.keremkulac.karakoctekstil.viewmodel.PatternPricesViewModel


class PatternPricesFragment : Fragment() {
    private val costPricesAdapter = CostPricesAdapter(arrayListOf())
    private lateinit var binding : FragmentPatternPricesBinding
    private lateinit var viewModel : PatternPricesViewModel
    private lateinit var list : ArrayList<HashMap<String,Any>>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Fiyat Listesi"
        viewModel = ViewModelProviders.of(this).get(PatternPricesViewModel::class.java)
        createRecyclerView()
        observeLiveData()
        setSearchMenu()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
       binding = FragmentPatternPricesBinding.inflate(inflater,container,false)
        refreshCostPrices()
        list = ArrayList()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        return binding.root
    }

    private  fun createRecyclerView(){
        binding.costPricesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.costPricesRecyclerView.adapter = costPricesAdapter
    }

    private fun refreshCostPrices(){
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.costPricesRecyclerView.visibility = View.GONE
            binding.costPricesError.visibility = View.GONE
            binding.costPricesLoading.visibility = View.VISIBLE
            viewModel.getCostPricessFromFirebase()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun observeLiveData(){
        viewModel.costPrices.observe(viewLifecycleOwner, Observer {costPrices->
            costPrices?.let {
                binding.costPricesRecyclerView.visibility = View.VISIBLE
                binding.costPricesError.visibility = View.GONE
                binding.costPricesLoading.visibility = View.GONE
                costPricesAdapter.updateCostPricesList(costPrices)
                list.addAll(costPrices)
            }
        })
    }

    private fun setSearchMenu(){
        val menuHost : MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu,menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.actionSearch -> {
                        val searchView = menuItem.actionView as androidx.appcompat.widget.SearchView
                        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
                            override fun onQueryTextSubmit(query: String?): Boolean {
                                return false
                            }
                            override fun onQueryTextChange(newText: String): Boolean {
                                viewModel.filter(newText,list,costPricesAdapter)
                                return true
                            }
                        })
                        true
                    }
                    else -> false
                }
            }

        },viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}