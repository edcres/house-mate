package com.aldreduser.housemate.ui.main.fragments.nestedfragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aldreduser.housemate.databinding.FragmentShoppingListBinding
import com.aldreduser.housemate.ui.main.adapters.ShoppingRecyclerviewListAdapter
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel

private const val TAG = "ShopListFragment__TAG"

class ShoppingListFragment : Fragment() {

    private var binding: FragmentShoppingListBinding? = null
    private val listsViewModel: ListsViewModel by activityViewModels()
    private lateinit var recyclerAdapter: ShoppingRecyclerviewListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentShoppingListBinding
            .inflate(inflater, container, false)
        binding = fragmentBinding
        recyclerAdapter =
            ShoppingRecyclerviewListAdapter(listsViewModel, viewLifecycleOwner, resources)
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated: ShoppingListFragment")
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            shoppingListRecyclerview.adapter = recyclerAdapter
            shoppingListRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        }
        listsViewModel.shoppingItems.observe(viewLifecycleOwner) { result ->
            // Get all shopping items.
            recyclerAdapter.submitList(result)
        }
    }

    override fun onResume() {
        super.onResume()
        listsViewModel.fragmentInView = TAG
        listsViewModel.listInView[0] = TAG
        listsViewModel.toggleHiddenTxt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        Log.i(TAG, "onDestroyView: ShoppingListFragment")
    }
}