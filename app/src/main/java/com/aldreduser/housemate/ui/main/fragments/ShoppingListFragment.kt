package com.aldreduser.housemate.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.databinding.FragmentShoppingListBinding
import com.aldreduser.housemate.ui.main.adapters.ShoppingRecyclerviewListAdapter
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel

class ShoppingListFragment : Fragment() {

    private var binding: FragmentShoppingListBinding? = null
    private val listsViewModel: ListsViewModel by activityViewModels()
    private lateinit var recyclerviewAdapter: ShoppingRecyclerviewListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentShoppingListBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = listsViewModel
            shoppingListFab.setOnClickListener { fabOnClick() }
        }

        //in a fragment, these don't belong inside the onCreate() function
        setupRecyclerView()
        setupViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    // CLICK HANDLERS //

    private fun fabOnClick() {
        // todo: pass some data to that fragment
        // navigate to the addListItem activity
    }

    // SETUP FUNCTIONS //

    // RecyclerView
    private fun setupRecyclerView() {
        // populate recyclerview
        binding?.shoppingListRecyclerview?.layoutManager = LinearLayoutManager(context)
        recyclerviewAdapter = ShoppingRecyclerviewListAdapter(arrayListOf())

        binding?.shoppingListRecyclerview?.addItemDecoration(
            DividerItemDecoration(
                binding?.shoppingListRecyclerview?.context,
                (binding?.shoppingListRecyclerview?.layoutManager as LinearLayoutManager).orientation
            )
        )
        binding?.shoppingListRecyclerview?.adapter = recyclerviewAdapter
    }

    // Populate Recyclerview
    private fun renderList(shoppingItems: List<ShoppingItem>) {
        recyclerviewAdapter.addData(shoppingItems)
        recyclerviewAdapter.notifyDataSetChanged()
    }

    // ViewModel
    private fun setupViewModel() {
//        // very important, declare which view-model interacts with this activity
//        listsViewModel = ViewModelProviders.of(
//            this,
//            listsViewModel(ApiHelper(ApiServiceImpl()))
//        ).get(ListsViewModel::class.java)
    }
}


// in case i need this later
//// Observer
//private fun setupObserver() {
//    listsViewModel.getChoreItems().observe(this, Observer {
//        when(it.status) {
//            Status.SUCCESS -> {
//                // when status is Success: hide bar
//                it.data?.let { choreItems -> renderList(choreItems) }
//                choresListRecyclerView.visibility = View.VISIBLE
//            }
//            Status.LOADING -> {
//                // when status is Loading: show progress bar
//                choresListRecyclerView.visibility = View.GONE
//            }
//            Status.ERROR -> {
//                // handle error (idk if the error is already handled or not)
//                // when status is error: hide progress bar
//                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
//            }
//        }
//    })
//}