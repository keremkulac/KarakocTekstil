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
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremkulac.karakoctekstil.R
import com.keremkulac.karakoctekstil.adapter.PatternAdapter
import com.keremkulac.karakoctekstil.databinding.FragmentPatternBinding
import com.keremkulac.karakoctekstil.model.Pattern
import com.keremkulac.karakoctekstil.viewmodel.PatternViewModel


class PatternFragment : Fragment(), PatternAdapter.ClickListener {

    private lateinit var binding : FragmentPatternBinding
    private lateinit var viewModel : PatternViewModel
    private lateinit var patternList : ArrayList<Pattern>
    private lateinit var patternAdapter : PatternAdapter
    private lateinit var selectedPattern : Pattern

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPatternBinding.inflate(inflater,container,false)
        patternList = ArrayList()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        return binding.root
    }

    private fun createRecyclerView(){
        binding.patternRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.patternRecyclerView.adapter = patternAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Desen Listesi"
        viewModel = ViewModelProviders.of(this).get(PatternViewModel::class.java)
        patternAdapter = PatternAdapter(this,arrayListOf(),requireActivity(),viewModel)
        createRecyclerView()
        refreshPatterns()
        observeLiveData()
        setSearchMenu()
    }

    private fun refreshPatterns(){
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.patternRecyclerView.visibility = View.GONE
            binding.patternError.visibility = View.GONE
            binding.patternLoading.visibility = View.VISIBLE
            viewModel.getPatternsFromFirebase()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun observeLiveData(){
        viewModel.patterns.observe(viewLifecycleOwner, Observer {patterns->
            patterns?.let {
                binding.patternRecyclerView.visibility = View.VISIBLE
                patternAdapter.updatePatternList(patterns)
                patternList.addAll(patterns)
            }
        })

        viewModel.patternError.observe(viewLifecycleOwner, Observer {error->
            error?.let {
                if(it){
                    binding.patternError.visibility = View.VISIBLE
                }else{
                    binding.patternError.visibility = View.GONE
                }
            }
        })
        viewModel.patternLoading.observe(viewLifecycleOwner, Observer { loading->
            loading?.let {
                if(it){
                    binding.patternLoading.visibility = View.VISIBLE
                    binding.patternRecyclerView.visibility = View.GONE
                    binding.patternError.visibility = View.GONE
                }else{
                    binding.patternLoading.visibility = View.GONE
                }
            }
        })
    }

    override fun ClickedItem(pattern: Pattern) {
        selectedPattern = pattern
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        val args = Bundle()
        val patternDetailFragment = PatternDetailFragment()
        args.putParcelable("pattern",pattern)
        patternDetailFragment.setArguments(args)
        fragmentTransaction.replace(R.id.mainFrameLayout,patternDetailFragment)
        fragmentTransaction.commit()
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
                                viewModel.filter(newText,patternList,patternAdapter)
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