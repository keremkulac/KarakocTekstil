package com.keremkulac.karakoctekstil.view

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.keremkulac.karakoctekstil.R
import com.keremkulac.karakoctekstil.adapter.OrderAdapter
import com.keremkulac.karakoctekstil.adapter.OrderTablayoutAdapter
import com.keremkulac.karakoctekstil.adapter.PatternAdapter
import com.keremkulac.karakoctekstil.databinding.FragmentOrderBinding
import com.keremkulac.karakoctekstil.model.Order
import com.keremkulac.karakoctekstil.viewmodel.OrderViewModel

class OrderFragment : Fragment() {

    private lateinit var binding : FragmentOrderBinding
    private val orderAdapter = OrderAdapter(arrayListOf())
    private lateinit var viewModel : OrderViewModel
    private lateinit var orderList : ArrayList<Order>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentOrderBinding.inflate(inflater,container,false)
        orderList = ArrayList()
        viewModel = ViewModelProviders.of(this).get(OrderViewModel::class.java)
        observeLiveData()
        //(activity as AppCompatActivity?)!!.supportActionBar!!.show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Siparişlerim"
        configureTopNavigation()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
      //  setSearchMenu()


    }

    private fun configureTopNavigation(){
        binding.viewpagerOrder.adapter = OrderTablayoutAdapter(childFragmentManager)
        binding.viewpagerOrder.offscreenPageLimit = 2
        binding.tabsOrder.setupWithViewPager(binding.viewpagerOrder)
    }
    private fun observeLiveData() {
        viewModel.orders.observe(viewLifecycleOwner, Observer { orders ->
            orders?.let {
              //  orderAdapter.updatePatternList(patterns)
                orderAdapter.updateOrderList(orders)
                orderList.addAll(orders)
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
                                viewModel.filter(newText,orderList,requireContext(),orderAdapter)
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
