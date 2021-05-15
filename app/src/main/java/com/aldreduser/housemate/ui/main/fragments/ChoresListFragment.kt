package com.aldreduser.housemate.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.databinding.FragmentChoresListBinding
import com.aldreduser.housemate.ui.main.adapters.ChoresRecyclerviewListAdapter
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel

class ChoresListFragment : Fragment() {

    private var binding: FragmentChoresListBinding? = null
    private val listsViewModel: ListsViewModel by activityViewModels()
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
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = listsViewModel
        }

        setupRecyclerView()
        setupViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    // CLICK HANDLERS //

    // SETUP FUNCTIONS //

    // RecyclerView
    private fun setupRecyclerView() {
        // populate recyclerview
        binding?.choresListRecyclerview?.layoutManager = LinearLayoutManager(context)
        recyclerviewAdapter = ChoresRecyclerviewListAdapter(arrayListOf())

        binding?.choresListRecyclerview?.addItemDecoration(
            DividerItemDecoration(
                binding?.choresListRecyclerview?.context,
                (binding?.choresListRecyclerview?.layoutManager as LinearLayoutManager).orientation
            )
        )
        binding?.choresListRecyclerview?.adapter = recyclerviewAdapter
    }

    // ViewModel
    private fun setupViewModel() {
//        // very important, declare which view-model interacts with this activity
//        listsViewModel = ViewModelProviders.of(
//            this,
//            listsViewModellFactory(ApiHelper(ApiServiceImpl()))
//        ).get(ListsViewModel::class.java)
    }

    // HELPER FUNCTIONS //

    // Populate Recyclerview
    private fun renderList(choreItems: List<ChoresItem>) {
        recyclerviewAdapter.addData(choreItems)
        recyclerviewAdapter.notifyDataSetChanged()
    }
}
