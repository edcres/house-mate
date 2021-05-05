package com.aldreduser.housemate.ui.main.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.view.ActionMode
import com.aldreduser.housemate.R
import com.aldreduser.housemate.databinding.ActivityMainBinding
import com.aldreduser.housemate.ui.main.adapters.ShoppingRecyclerviewListAdapter
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import kotlinx.android.synthetic.main.activity_main.*

// todo: instantiate objects

// ui
// todo: shopping list and chores list should be in their own fragment
// todo: make navigation tabs with viewpager2

// storage
//remote
// todo: use remote storage in shopping items
// todo: use web service to interact with remote data (maybe: firebase, mySQL). (maybe also a REST API)
// todo: edit: api folder files, repository, viewmodel/viewmodelfactory
// todo: when getting data from remote storage, edit 'ApiServiceImpl' file
//local + remote
// Data in the remote database is a priority bc different users will be interacting with it.
//  - compare local database to remote database for data differences, update local database with differences
//  - if there's a conflict with the same item ask user if they want to overwrite it with his local database data
//  - SQLLite for local storage and use Firebase for syncing
//    -just translate record updates to JSON and push them out, and implement handlers that update the
//     local database from the JSON they receive.
// todo: conncet local to remote repository (rn chores has local, shopping items has remote)
// -get @Update and @Delete implemented throughout the app https://www.youtube.com/watch?v=5rfBU75sguk
// -database with multiple entities   https://kirillsuslov.medium.com/how-to-add-more-that-one-entity-in-room-5cc3743219c0
// todo: maybe it's better to just use firebase remotely and locally and get rid of room (use room in another app)
//  -might need to use Room here if the user opens the app and doesn't have access to the network.
//  -then update the changes to remote storage (from Room) when user has access to the network and the app is open.
//      (this is if i can't update the database through firebase locally and without network access)

// Contextual actionbar bug
// todo: fix contextual actionbar bug. Its activates by default at the beginning of the app

// Fragments
// todo: there should probably be a fragment for shopping list and another for chores

// recyclerview
// todo: continue the codelab at part 11 ->    https://developer.android.com/codelabs/android-room-with-a-view-kotlin/#9
// recyclerview codelab https://developer.android.com/codelabs/kotlin-android-training-recyclerview-fundamentals#0
// todo: have placeholder data to get from storage, before getting it remotely
// todo: adjust recyclerview to work with the items in my own app
// todo: change recyclerview and make it have clickable buttons and stuff       -->       https://material.io/components/cards  https://material.io/components/lists#anatomy
//      - long click on recycler items to engage 'contextual actionbar' https://developer.android.com/guide/topics/ui/menus
// todo: more options 3dots at top right of toolbar to select multiple items to delete (maybe also duplicate)
//  -this is a contextual action bar, so recycler items can be selected and choose to be deleted through the actionbar
// todo: 'more options' icon pops up a select box in each recycler item, each selected has the option to be deleted or duplicated.
//  alternatively: use contextual action bar to long click items, these can be deleted or duplicated

// dataBinding
// todo: make all necessary data dataBindable
//  -im using kotlin flow (new), will maybe also use liveData
//  enable dataBinding, wrap layout root element in <layout>
//  layout variables in <data>
//  layout expressions and element attributes: @{expression}
// Todo: use LiveData for observables
//https://www.youtube.com/watch?v=omml4lK_b-A&t=509s
//https://developer.android.com/topic/libraries/data-binding/
//databinding codelab: https://developer.android.com/codelabs/android-databinding#0

// user
// todo: In the home activity, have the user put in his name so it is displayed in 'added by:' (saved in shared preferences)
//  user can choose anonymous
// todo: in home activity, check if user has name registered (in shared preferences), if not ask for name
// todo: make feature so that user can change their name later on

// navigation
// todo: (make sure this is good) navigation and arrow icon in all activities (except the one that opens when the app opens)
// todo: when user backs out of adding a new item, ask if they sure they wanna cancel. Might have to learn about activity lifecycles
// todo: when user goes back in navigation from 'add shoppingList item activity', app asks to cancel adding new activity

// todo: when the app opens, go to the shopping screen seamlessly. Or the home screen if necessary

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

/*
         todo: check if user already input a name previously (from shared preferences) (have the logic in a viewmodel)
          if not send to shopping list activity (or last activity visited)
         todo: ask user to put their name (so that others can see who added to do items)
          user can choose anon
*/

// better MVVM architecture
// todo: do the comments below to improve the architecture (unless already used or impractical/unnecessary)
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

//future
// if more lists are added, make base classes
// set icons on the material inputs

// Home Screen
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val sharedViewModel: ListsViewModel by viewModels()
    private lateinit var adapterRecyclerview: ShoppingRecyclerviewListAdapter       // for RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding?.apply {
            lifecycleOwner = this@MainActivity
            viewModel = sharedViewModel
        }
        setUpAppBar()
        setUpContextualAppBar()
    }

    private fun setUpAppBar() {
        val moreOptionsDrawable = R.drawable.ic_more_options_24dp
        binding.homeScreenTopAppbar.title = "House Mate"
        // overflow icon is only changed to the drawables of android version is lollipop or above
        // (~Samsung Galaxy S6 and above. Current in 2021 is Samsung Galaxy S21)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            binding.homeScreenTopAppbar.overflowIcon = getDrawable(moreOptionsDrawable) // might have to do this in every activity.
        }
        binding.homeScreenTopAppbar.setNavigationOnClickListener {
            //todo: handle navigation icon press
            //the navigation icon is the icon to the left
            // command+f 'Navigation icon attributes' in material design website
        }

        binding.homeScreenTopAppbar.setOnMenuItemClickListener { menuItem ->
            val shoppingListEdit = R.id.shopping_list_edit
            val shoppingListOptionDuplicate = R.id.shopping_list_option_duplicate
            val shoppingListOptionDelete = R.id.shopping_list_option_delete

            when (menuItem.itemId) {
                shoppingListEdit -> {
                    //todo: handle edit icon press
                    //user presses this icon and edit icons pop up next to each recyclerView item
                    true
                } shoppingListOptionDuplicate -> {
                    //todo: add functionality
                    //might use it as an alternative to contextual action bar. In case the user can't figure out how to use the contextual action bar
                    true
                } shoppingListOptionDelete -> {
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
        val contextualActionBar = R.menu.contextual_action_bar
        val contextualDuplicate = R.id.contextual_duplicate
        val contextualDelete = R.id.contextual_delete


        // this might not work bc it's in a function, but i think it will
        val callback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                menuInflater.inflate(contextualActionBar, menu)
                return true
            }
            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }
            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return when (item?.itemId) {
                    contextualDuplicate -> {
                        // todo: Handle duplicate icon press
                        true
                    } contextualDelete -> {
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
}
