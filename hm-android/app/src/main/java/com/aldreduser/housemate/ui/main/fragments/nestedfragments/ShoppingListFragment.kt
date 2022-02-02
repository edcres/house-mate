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
import com.aldreduser.housemate.databinding.FragmentShoppingListBinding
import com.aldreduser.housemate.ui.main.adapters.ShoppingRecyclerviewListAdapter
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel

class ShoppingListFragment : Fragment() {

    private val fragmentTag = "ShoppingListFragmentTAG"
    private var binding: FragmentShoppingListBinding? = null
    private val listsViewModel: ListsViewModel by activityViewModels()
    private lateinit var recyclerAdapter: ShoppingRecyclerviewListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(fragmentTag, "onCreateView: ShoppingListFragment")
        val fragmentBinding = FragmentShoppingListBinding
            .inflate(inflater, container, false)
        binding = fragmentBinding
        recyclerAdapter = ShoppingRecyclerviewListAdapter(listsViewModel, viewLifecycleOwner)
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i(fragmentTag, "onViewCreated: ShoppingListFragment")
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = listsViewModel
            // In a fragment, these don't belong inside the onCreate() function
            shoppingListRecyclerview.adapter = recyclerAdapter
            shoppingListRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        }
        // Update recyclerView
        listsViewModel.shoppingItems.observe(viewLifecycleOwner, Observer { result ->
            recyclerAdapter.submitList(result)
        })
    }

    override fun onResume() {
        super.onResume()
        listsViewModel.fragmentInView = fragmentTag
        listsViewModel.listInView[0] = fragmentTag
    }

    override fun onDestroyView() {
        listsViewModel.sendShoppingVolunteersToDb()
        binding = null
        Log.i(fragmentTag, "onDestroyView: ShoppingListFragment")
        super.onDestroyView()
    }
}
