package com.keremkulac.karakoctekstil.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremkulac.karakoctekstil.R
import com.keremkulac.karakoctekstil.adapter.OngoingOrderAdapter
import com.keremkulac.karakoctekstil.databinding.FragmentOngoingOrderBinding
import com.keremkulac.karakoctekstil.model.Order
import com.keremkulac.karakoctekstil.viewmodel.OngoingOrderViewModel


class OngoingOrderFragment : Fragment() {

    private lateinit var binding : FragmentOngoingOrderBinding
    private lateinit var viewModel : OngoingOrderViewModel
    private lateinit var ongoingOrderAdapter : OngoingOrderAdapter
    private lateinit var orderList : ArrayList<Order>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OngoingOrderViewModel::class.java)
        ongoingOrderAdapter = OngoingOrderAdapter(requireContext(),requireActivity(),viewModel,arrayListOf())
        createRecyclerView()
        refreshPatterns()
        observeLiveData()
      //  setSearchMenu()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentOngoingOrderBinding.inflate(inflater,container,false)
        orderList = ArrayList()
        return binding.root
    }


  private  fun createRecyclerView(){
      binding.ongoingOrderRecyclerView.layoutManager = LinearLayoutManager(context)
      binding.ongoingOrderRecyclerView.adapter = ongoingOrderAdapter
  }

  private fun refreshPatterns(){
      binding.ongoingOrderSwipeRefreshLayout.setOnRefreshListener {
          binding.ongoingOrderRecyclerView.visibility = View.GONE
          binding.ongoingOrderError.visibility = View.GONE
          binding.ongoingOrderLoading.visibility = View.VISIBLE
          viewModel.getOrdersFromFirebase()
          binding.ongoingOrderSwipeRefreshLayout.isRefreshing = false
      }
  }


    private fun observeLiveData(){
        viewModel.orders.observe(viewLifecycleOwner, Observer {orders->
            orders?.let{
                binding.ongoingOrderRecyclerView.visibility = View.VISIBLE
                binding.ongoingOrderError.visibility = View.GONE
                binding.ongoingOrderLoading.visibility = View.GONE
                ongoingOrderAdapter.updateOrderList(orders)
                orderList.addAll(orders)
            }
        })
    }
    private fun decrease(){
     //   viewModel.decreasePiece("61c2f485-4ff4-49ac-ac3e-ab322380c333")
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
                                viewModel.filter(newText,orderList,requireContext(),ongoingOrderAdapter)
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