package com.aldreduser.housemate.ui.main.fragments.nestedfragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aldreduser.housemate.databinding.FragmentChoresListBinding
import com.aldreduser.housemate.ui.main.adapters.ChoresRecyclerviewListAdapter
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel

private const val TAG = "ChoresListFragment__TAG"

class ChoresListFragment : Fragment() {
    private var binding: FragmentChoresListBinding? = null
    private val listsViewModel: ListsViewModel by activityViewModels()
    private lateinit var recyclerAdapter: ChoresRecyclerviewListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentChoresListBinding
            .inflate(inflater, container, false)
        binding = fragmentBinding
        recyclerAdapter =
            ChoresRecyclerviewListAdapter(listsViewModel, viewLifecycleOwner, resources)
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated: ChoresListFragment")
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            choresListRecyclerview.adapter = recyclerAdapter
            choresListRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        }
        listsViewModel.choreItems.observe(viewLifecycleOwner) { result ->
            // Get all shopping items.
            recyclerAdapter.submitList(result)
        }
    }

    override fun onResume() {
        super.onResume()
        listsViewModel.fragmentInView = TAG
        listsViewModel.listInView[1] = TAG
        listsViewModel.toggleHiddenTxt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        Log.i(TAG, "onDestroyView: ChoresListFragment")
    }
}
