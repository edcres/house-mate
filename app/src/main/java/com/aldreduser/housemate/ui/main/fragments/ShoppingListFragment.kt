package com.aldreduser.housemate.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aldreduser.housemate.R
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.data.model.remote.api.ApiHelper
import com.aldreduser.housemate.data.model.remote.api.ApiServiceImpl
import com.aldreduser.housemate.ui.main.adapters.ShoppingRecyclerviewListAdapter
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import kotlinx.android.synthetic.main.fragment_shopping_list.*

class ShoppingListFragment : Fragment() {

    private val listsViewModel: ListsViewModel by activityViewModels()
    private lateinit var adapterRecyclerview: ShoppingRecyclerviewListAdapter       // for RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //in a fragment, these don't belong inside the onCreate() function
        setupRecyclerView()
        setupViewModel()
        fabOnClick()
    }

    private fun fabOnClick() {
        // todo: pass some data to that activity
        // navigate to the  add shoppingList item activity
        shoppingListFab.setOnClickListener() {

            //todo: navigate to: AddShoppingItemActivity
        }
    }

    // RecyclerView
    private fun setupRecyclerView() {
        // populate recyclerview
        shoppingListRecyclerView.layoutManager = LinearLayoutManager(context)  //todo: possible bug: 'context' was 'this'
        adapterRecyclerview = ShoppingRecyclerviewListAdapter(arrayListOf())

        shoppingListRecyclerView.addItemDecoration(
            DividerItemDecoration(
                shoppingListRecyclerView.context,
                (shoppingListRecyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        shoppingListRecyclerView.adapter = adapterRecyclerview
    }
    // Populate Recyclerview
    private fun renderList(shoppingItems: List<ShoppingItem>) {
        adapterRecyclerview.addData(shoppingItems)
        adapterRecyclerview.notifyDataSetChanged()
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