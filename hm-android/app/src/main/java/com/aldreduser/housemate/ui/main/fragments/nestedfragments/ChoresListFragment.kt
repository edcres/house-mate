package com.aldreduser.housemate.ui.main.fragments.nestedfragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aldreduser.housemate.databinding.FragmentChoresListBinding
import com.aldreduser.housemate.ui.main.adapters.ChoresRecyclerviewListAdapter
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel

class ChoresListFragment : Fragment() {

    private val fragmentTag = "ChoresListFragmentTAG"
    private var binding: FragmentChoresListBinding? = null
    private val listsViewModel: ListsViewModel by activityViewModels()
    private val recyclerAdapter = ChoresRecyclerviewListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(fragmentTag, "onCreateView: ChoresListFragment")
        val fragmentBinding = FragmentChoresListBinding
            .inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i(fragmentTag, "onViewCreated: ChoresListFragment")
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = listsViewModel
            choresListRecyclerview.adapter = recyclerAdapter
            choresListRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        }
        listsViewModel.choreItems.observe(viewLifecycleOwner, Observer { result ->
            recyclerAdapter.submitList(result)
        })
    }

    override fun onResume() {
        super.onResume()
        listsViewModel.fragmentInView = fragmentTag
        listsViewModel.listInView[1] = fragmentTag
    }

    override fun onDestroyView() {
        listsViewModel.sendChoresVolunteersToDb()
        binding = null
        Log.i(fragmentTag, "onDestroyView: ChoresListFragment")
        super.onDestroyView()
    }
}
