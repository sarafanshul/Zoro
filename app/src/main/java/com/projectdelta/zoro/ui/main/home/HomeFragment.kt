package com.projectdelta.zoro.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.projectdelta.zoro.data.model.User
import com.projectdelta.zoro.databinding.FragmentHomeBinding
import com.projectdelta.zoro.ui.base.BaseViewBindingFragment
import com.projectdelta.zoro.ui.main.MainViewModel
import com.projectdelta.zoro.ui.main.home.adapter.HomeRecyclerViewAdapter
import com.projectdelta.zoro.util.system.lang.collectLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import timber.log.Timber

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : BaseViewBindingFragment<FragmentHomeBinding>() {

    private var adapter : HomeRecyclerViewAdapter? = null

    private val activityViewModel : MainViewModel by activityViewModels()

    private val viewModel : HomeViewModel by viewModels()

    private var users : Flow<List<User>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = HomeRecyclerViewAdapter {
            Timber.d(it.toString())
        }

        users = viewModel.friends.combine(activityViewModel.listData){ friends ,data ->
            friends.map { user ->
                user.messages = data[user.id!!]?.size
            }
            friends
        }
    }

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.userRv.adapter = adapter

        users?.let { x -> collectLifecycleFlow(x) {
            adapter?.submitList(it)
        } }
    }

    override fun onDestroyView() {
        binding.userRv.adapter = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        adapter = null
        users = null
        super.onDestroy()
    }
}