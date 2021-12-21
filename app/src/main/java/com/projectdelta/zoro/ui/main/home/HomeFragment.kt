package com.projectdelta.zoro.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import com.projectdelta.zoro.R
import com.projectdelta.zoro.data.model.User
import com.projectdelta.zoro.databinding.FragmentHomeBinding
import com.projectdelta.zoro.ui.base.BaseViewBindingFragment
import com.projectdelta.zoro.ui.main.home.adapter.HomeRecyclerViewAdapter
import com.projectdelta.zoro.util.system.lang.collectLatestLifecycleFlow
import com.projectdelta.zoro.util.system.lang.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : BaseViewBindingFragment<FragmentHomeBinding>() {

    private var adapter : HomeRecyclerViewAdapter? = null

    private val viewModel : HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = HomeRecyclerViewAdapter { user ->
            navigateChatFragment(user)
        }

        viewModel.updateQuery("")
    }

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerObservers()

        initUI()

    }

    private fun initUI(){
        setSearchBar()

        binding.userRv.adapter = adapter
    }

    private fun setSearchBar() {
        val searchView : SearchView? = binding.toolbar.menu.findItem(R.id.action_search).actionView as SearchView?
        searchView?.queryHint = "Contact name..."

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.updateQuery(newText.orEmpty().trim())
                return false
            }
        })
    }

    private fun registerObservers(){
        collectLatestLifecycleFlow(viewModel.userData){
            adapter?.submitList(it)
        }
    }

    private fun navigateChatFragment( user : User ) {
        val action = HomeFragmentDirections
            .actionHomeFragmentToChatFragment(user)
        safeNavigate(action)
    }

    override fun onDestroyView() {
        binding.userRv.adapter = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        adapter = null
        super.onDestroy()
    }
}