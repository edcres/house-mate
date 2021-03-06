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
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.data.model.room.ListsRoomDatabase
import com.aldreduser.housemate.databinding.FragmentShoppingListBinding
import com.aldreduser.housemate.ui.main.adapters.ShoppingRecyclerviewListAdapter
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModelFactory

class ShoppingListFragment : Fragment() {

    private var binding: FragmentShoppingListBinding? = null
    private lateinit var listsViewModel: ListsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentShoppingListBinding.inflate(inflater, container, false)
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

        //in a fragment, these don't belong inside the onCreate() function
        setUpRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

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
        val adapter = ShoppingRecyclerviewListAdapter()
        binding?.shoppingListRecyclerview?.adapter = adapter
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