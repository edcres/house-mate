package com.aldreduser.housemate.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aldreduser.housemate.R
import com.aldreduser.housemate.data.api.ApiHelper
import com.aldreduser.housemate.data.api.ApiServiceImpl
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.ui.base.ViewModelFactory
import com.aldreduser.housemate.ui.main.adapter.MainAdapter
import com.aldreduser.housemate.ui.main.viewmodel.MainViewModel
import com.aldreduser.housemate.utils.Status
import kotlinx.android.synthetic.main.activity_main.*

// https://blog.mindorks.com/mvvm-architecture-android-tutorial-for-beginners-step-by-step-guide

// ui
// todo: change the main activity UI, make it more tailored to my app
//  'edit' button to the right of toolbar
//  edit the style of the app (colors, etc)
//  navigation in all activities (except the one that opens when the app opens): navigation icon - back arrow to the left
//  floating action button to go to new shopping item activity     -->      https://material.io/components/buttons-floating-action-button/android#regular-fabs
// todo: make an add shoppingList activity
//  change shopping item activity title
//  when user click '+' button, app goes to shopping item activity
//  when user goes back in navigation, app asks to cancel adding new activity
//  flatting point to add changes to storage (no functionality yet)
// todo: change shopping item UI, according to my own design

// storage
// todo: make room with a view database first (i think it's only local storage)
// todo: look up how to do the local and remote part of the data package, and how to connect it
// todo: when getting data from remote storage, edit 'ApiServiceImpl' file

// recyclerview
// todo: have placeholder data to get from storage, before getting it remotely
// todo: adjust recyclerview to work with the items in my own app
// todo: change recyclerview and make it have clickable buttons and stuff       -->       https://material.io/components/cards
// todo: more options 3dots at top right of toolbar to select multiple items to delete (maybe also duplicate)
//  -this is a contextual action bar, so recycler items can be selected and choose to be deleted through the actionbar

/*
 chore item
 todo: add the things for the chore item later
    -Make tabs to switch between shopping items and chore items
        -to differentiate an active tab from an inactive tab, apply an underline and color change to the active tab’s text and icon.   -->  https://material.io/design/navigation/understanding-navigation.html#lateral-navigation
    -make a model for it
    -make an item layout
    -make an add shoppingList activity
    -adjust recyclerview (or make a new one for Chores)

 todo: when i start implementing chore items, command+f 'ShoppingItem' throughout the project
 */

// better MVVM architecture
// todo: do the comments at the bottom of this page to improve the architecture

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: MainAdapter       // for RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //todo: eventually when adding chore items, put the code in this activity to a shopping items activity
        setUpAppBar()
        setupUI()
        setupViewModel()
        setupObserver()
    }

    private fun setUpAppBar() {
        topAppBar.title = "Shopping Items"
        topAppBar.setNavigationOnClickListener {
            //todo: handle navigation icon press
            //the navigation icon is the icon to the left
            // command+f 'Navigation icon attributes' in material design website
        }

        topAppBar.setOnMenuItemClickListener {menuItem ->
            when (menuItem.itemId) {
                R.id.edit -> {
                    //todo: handle edit icon press
                    //user presses this icon and edit icons pop up next to each recyclerView item
                    true
                }
                R.id.more_options -> {
                    //todo: if this will not be given functionality, delete it
                    //might use it as an alternative to contextual action bar. In case the user can't figure out how to use the contextual action bar
                    true
                }
                else -> false
            }
        }
    }

    // UI
    private fun setupUI() {
        // populate recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(arrayListOf())

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
    }

    // observe data
    private fun setupObserver() {
        mainViewModel.getShoppingItems().observe(this, Observer {
            when(it.status) {
                Status.SUCCESS -> {
                    // when status is Success: hide bar
                    it.data?.let { shoppingItems -> renderList(shoppingItems) }
                    recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    // when status is Loading: show progress bar
                    recyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    // handle error (idk if the error is already handled or not)
                    // when status is error: hide progress bar
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT)

                }
            }
        })
    }
    // put items in the recyclerview
    private fun renderList(shoppingItems: List<ShoppingItem>) {
        adapter.addData(shoppingItems)
        adapter.notifyDataSetChanged()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(MainViewModel::class.java)

    }

}

/*
Improve architecture by:
-Implement Dependency Inject Framework - Dagger in the project.
-Make ApiService Singleton and reuse the same instance for all the features.
-Create base classes such as BaseActivity.
-Handle all the API errors at a single place in a better way.
-Create Interfaces for the classes wherever required.
-You can learn Kotlin Coroutines from here step by step and migrate to Coroutines.
-Take advantage of Android KTX - Kotlin Extensions.
-Write Unit-Test
-and so on.
*/