package com.aldreduser.housemate.ui.main.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.view.ActionMode
//import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aldreduser.housemate.AddShoppingItemActivity
import com.aldreduser.housemate.R
import com.aldreduser.housemate.data.model.api.ApiHelper
import com.aldreduser.housemate.data.model.api.ApiServiceImpl
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.ui.base.ViewModelFactory
import com.aldreduser.housemate.ui.main.adapter.MainAdapter
import com.aldreduser.housemate.ui.main.viewmodel.MainViewModel
import com.aldreduser.housemate.util.Status
import kotlinx.android.synthetic.main.activity_main.*

// ui
// todo: change shopping item UI, according to my own design

// viewModel
// todo: learn about viewModels

// storage
// todo: make room with a view database first (i think it's only local storage)
// todo: look up how to do the local and remote part of the data package, and how to connect it
// todo: when getting data from remote storage, edit 'ApiServiceImpl' file

// recyclerview
// todo: have placeholder data to get from storage, before getting it remotely
// todo: adjust recyclerview to work with the items in my own app
// todo: change recyclerview and make it have clickable buttons and stuff       -->       https://material.io/components/cards
//      - long click on recycler items to engage 'contextual actionbar' https://developer.android.com/guide/topics/ui/menus
// todo: more options 3dots at top right of toolbar to select multiple items to delete (maybe also duplicate)
//  -this is a contextual action bar, so recycler items can be selected and choose to be deleted through the actionbar
// todo: 'more options' icon pops up a select box in each recycler item, each selected has the option to be deleted or duplicated.
//  alternatively: use contextual action bar to long click items, these can be deleted or duplicated

// dataBinding
// todo: make all necessary data dataBindable
//  enable dataBinding, wrap layout root element in <layout>
//  layout variables in <data>
//  layout expressions and element attributes: @{expression}
// Todo: use LiveData for observables
//https://www.youtube.com/watch?v=omml4lK_b-A&t=509s
//https://developer.android.com/topic/libraries/data-binding/
//https://developer.android.com/codelabs/android-databinding#0

// navigation
// todo: (make sure this is good) navigation and arrow icon in all activities (except the one that opens when the app opens)
// todo: when user backs out of adding a new item, ask if they sure they wanna cancel. Might have to learn about activity lifecycles
// todo: when user goes back in navigation from 'add shoppingList item activity', app asks to cancel adding new activity

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
        setUpContextualAppBar()
        setupRecyclerView()
        setupViewModel()
        setupObserver()
        fabOnClick()
    }

    private fun fabOnClick() {
        // navigate to the  add shoppingList item activity
        fab.setOnClickListener() {
            val newIntent = Intent(this, AddShoppingItemActivity::class.java)
            startActivity(newIntent)
        }
    }

    private fun setUpAppBar() {
        topAppBar.title = "Shopping Items"
        // overflow icon is only changed to the drawables of android version is lollipop or above
        // (~Samsung Galaxy S6 and above. Current in 2021 is Samsung Galaxy S21)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            topAppBar.overflowIcon = getDrawable(R.drawable.ic_more_options_24dp) // might have to do this in every activity.
        }
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
                R.id.option_duplicate -> {
                    //todo: add functionality
                    //might use it as an alternative to contextual action bar. In case the user can't figure out how to use the contextual action bar
                    true
                }R.id.option_delete -> {
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

    // Observer
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
    // Populate Recyclerview
    private fun renderList(shoppingItems: List<ShoppingItem>) {
        adapter.addData(shoppingItems)
        adapter.notifyDataSetChanged()
    }
    // ViewModel
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