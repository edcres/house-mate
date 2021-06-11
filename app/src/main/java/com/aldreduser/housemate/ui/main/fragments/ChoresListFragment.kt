package com.aldreduser.housemate.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aldreduser.housemate.data.ListsRepository
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.data.model.room.ListsRoomDatabase
import com.aldreduser.housemate.databinding.FragmentChoresListBinding
import com.aldreduser.housemate.ui.main.adapters.ChoresRecyclerviewListAdapter
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModelFactory

class ChoresListFragment : Fragment() {

    private var binding: FragmentChoresListBinding? = null
    private lateinit var listsViewModel: ListsViewModel
    private lateinit var recyclerviewAdapter: ChoresRecyclerviewListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentChoresListBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        setUpViewModel()
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = listsViewModel
        }

        setUpRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    // CLICK HANDLERS //

    // SET UP FUNCTIONS //
    private fun setUpViewModel() {
        val application = requireNotNull(this.activity).application
        val database = ListsRoomDatabase.getInstance(application)
        val repository = ListsRepository.getInstance(database)
        val viewModelFactory = ListsViewModelFactory(repository, application)
        listsViewModel = ViewModelProvider(
            this, viewModelFactory).get(ListsViewModel::class.java)
    }

    private fun setUpRecyclerView() {
        val adapter = ChoresRecyclerviewListAdapter()
        binding?.choresListRecyclerview?.adapter = adapter
    }
}
