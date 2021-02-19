package com.aldreduser.housemate.ui.main.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.view.ActionMode
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aldreduser.housemate.AddShoppingItemActivity
import com.aldreduser.housemate.R
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.data.model.api.ApiHelper
import com.aldreduser.housemate.data.model.api.ApiServiceImpl
import com.aldreduser.housemate.ui.base.ShoppingListViewModelFactory
import com.aldreduser.housemate.ui.main.adapter.ShoppingListAdapter
import com.aldreduser.housemate.ui.main.viewmodels.ShoppingListViewModel
import com.aldreduser.housemate.util.Status
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_shopping_list.*

class ShoppingListActivity : AppCompatActivity() {

    private lateinit var shoppingListViewModel: ShoppingListViewModel
    private lateinit var adapter: ShoppingListAdapter       // for RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        setUpAppBar()
        setUpContextualAppBar()
        setupRecyclerView()
        setupViewModel()
        setupObserver()

        fabOnClick()
    }

    private fun fabOnClick() {
        // navigate to the  add shoppingList item activity
        shoppingListFab.setOnClickListener() {
            val newIntent = Intent(this, AddShoppingItemActivity::class.java)
            startActivity(newIntent)
        }
    }

    private fun setUpAppBar() {
        shoppingListTopAppBar.title = "Shopping Items"
        // overflow icon is only changed to the drawables of android version is lollipop or above
        // (~Samsung Galaxy S6 and above. Current in 2021 is Samsung Galaxy S21)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            shoppingListTopAppBar.overflowIcon = getDrawable(R.drawable.ic_more_options_24dp) // might have to do this in every activity.
        }
        shoppingListTopAppBar.setNavigationOnClickListener {
            //todo: handle navigation icon press
            //the navigation icon is the icon to the left
            // command+f 'Navigation icon attributes' in material design website
        }

        shoppingListTopAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.shopping_list_edit -> {
                    //todo: handle edit icon press
                    //user presses this icon and edit icons pop up next to each recyclerView item
                    true
                }
                R.id.shopping_list_option_duplicate -> {
                    //todo: add functionality
                    //might use it as an alternative to contextual action bar. In case the user can't figure out how to use the contextual action bar
                    true
                }R.id.shopping_list_option_delete -> {
                //todo: add functionality
                //might use it as an alternative to contextual action bar. In case the user can't figure out how to use the contextual action bar
                true
            }
                else -> false
            }
        }
    }

    // Contextual Action Bar
    // ActionMode.Callback is to invoke the contextual action mode only when the user selects specific views
    private fun setUpContextualAppBar() {
        // this might not work bc it's in a function, but i think it will
        val callback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                menuInflater.inflate(R.menu.contextual_action_bar, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return when (item?.itemId) {
                    R.id.contextual_duplicate -> {
                        // todo: Handle duplicate icon press
                        true
                    }R.id.contextual_delete -> {
                        // todo: Handle delete icon press
                        true
                    }
                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
            }
        }
        val actionMode = startSupportActionMode(callback)   //I changed the import from 'android.view.ActionMode.Callback' to 'androidx.appcompat.view.ActionMode.Callback'
        actionMode?.title = "1 selected"
    }

    // RecyclerView
    private fun setupRecyclerView() {
        // populate recyclerview
        shoppingListRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ShoppingListAdapter(arrayListOf())

        shoppingListRecyclerView.addItemDecoration(
            DividerItemDecoration(
                shoppingListRecyclerView.context,
                (shoppingListRecyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        shoppingListRecyclerView.adapter = adapter
    }

    // Observer
    private fun setupObserver() {
        shoppingListViewModel.getShoppingItems().observe(this, Observer {
            when(it.status) {
                Status.SUCCESS -> {
                    // when status is Success: hide bar
                    it.data?.let { shoppingItems -> renderList(shoppingItems) }
                    shoppingListRecyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    // when status is Loading: show progress bar
                    shoppingListRecyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    // handle error (idk if the error is already handled or not)
                    // when status is error: hide progress bar
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    // Populate Recyclerview
    private fun renderList(shoppingItems: List<ShoppingItem>) {
        adapter.addData(shoppingItems)
        adapter.notifyDataSetChanged()
    }
    // ViewModel
    private fun setupViewModel() {
        // very important, declare which view-model interacts with this activity
        shoppingListViewModel = ViewModelProviders.of(
            this,
            ShoppingListViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(ShoppingListViewModel::class.java)
    }

}