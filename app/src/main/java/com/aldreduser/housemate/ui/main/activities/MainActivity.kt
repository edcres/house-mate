package com.aldreduser.housemate.ui.main.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.view.ActionMode
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aldreduser.housemate.R
import com.aldreduser.housemate.data.ListsRepository
import com.aldreduser.housemate.data.model.room.ListsRoomDatabase
import com.aldreduser.housemate.databinding.ActivityMainBinding
import com.aldreduser.housemate.ui.main.adapters.ShoppingRecyclerviewListAdapter
import com.aldreduser.housemate.ui.main.fragments.ChoresListFragment
import com.aldreduser.housemate.ui.main.fragments.ShoppingListFragment
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

// todo: recyclerview
// Adapter:
// -create it
// -dataBinding
// DataBinding in the recycler items might be wrong.

// todo: ViewPager2

// todo: learn about databases multiple entities with relations

// todo: AddListItemFragment should probably be an activity.

// todo: tab onclick dataBinding bug:
// -solution: try to fix after making the viewpager, or don't use dataBinding and set the click listener through the kotlin file

// Contextual actionbar bug
// its probably bc I have the entire set up function for it commented out
// todo: fix contextual actionbar bug. Its activates by default at the beginning of the app

// recyclerview
// todo: contexcual actionBar: long click on recycler items to engage 'contextual actionbar' https://developer.android.com/guide/topics/ui/menus
// more options 3dots at top right of toolbar to select multiple items to delete (maybe also duplicate)
// -this is a contextual action bar, so recycler items can be selected and choose to be deleted through the actionbar
// todo: 'more options' icon pops up a select box in each recycler item, each selected has the option to be deleted or duplicated.
// alternatively: use contextual action bar to long click items, these can be deleted or duplicated

// storage
// todo: remote
// use firebase to store remote data (local database will be used as a cache)
// edit: api folder files, repository, viewmodel/viewmodelfactory
// when getting data from remote storage, edit 'ApiServiceImpl' file
// todo: local + remote
// https://developer.android.com/codelabs/kotlin-android-training-repository?index=..%2F..android-kotlin-fundamentals#7
// Data in the remote database is a priority bc different users will be interacting with it.
//  - compare local database to remote database for data differences, update local database with differences
//  - if there's a conflict with the same item ask user if they want to overwrite it with his local database data
// -database with multiple entities   https://kirillsuslov.medium.com/how-to-add-more-that-one-entity-in-room-5cc3743219c0
// -maybe update the changes to remote storage (from Room) when user has access to the network and the app is open.

// todo: user
// In the home activity, have the user put in his name so it is displayed in 'added by:' (saved in shared preferences)
// user can choose anonymous
// in home activity, check if user has name registered (in shared preferences), if not ask for name
// make feature so that user can change their name later on

// todo: navigation
// (make sure this is good) navigation and arrow icon in all activities (except the one that opens when the app opens)
// when user backs out of adding a new item, ask if they're sure they wanna cancel.
// when user goes back in navigation from 'add shoppingList item activity', app asks to cancel adding new activity

// todo: clean up unused imports
// todo: take care of warnings
// todo: clean up comments

/*
Improve MVVM architecture by:
-Implement Dependency Inject Framework - Dagger in the project.
-Create base classes such as BaseActivity.
-Create Interfaces for the classes wherever required. (maybe)
-Take advantage of Android KTX - Kotlin Extensions.
-Write Unit-Test
-and so on.
*/

//future
// User can add many more sets (Maybe cap it at some point.)
//      -have reusable layouts, databind it, and make it work with the remote and local repos
// if more lists are added, make base classes
// set icons on the material inputs
// for the cost in shopping list, have a converter so it displays the currency, and get the correct currency
// make when neededDoneBy into a date picker

// Home Screen
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private lateinit var listsViewModel: ListsViewModel
    private val tabTitles = arrayOf("Shopping List", "Chores")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setUpViewModel()
        binding?.apply {
            lifecycleOwner = this@MainActivity
            viewModel = listsViewModel         // todo: bug here
            addItemListFab.setOnClickListener { fabOnClick() }
        }
        setUpAppBar()
        setUpContextualAppBar()
        setUpTabs()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    // CLICK LISTENERS //
    // handle fab click
    private fun fabOnClick() {
        // add workout
        // todo: handle click
    }

    // SET UP FUNCTIONS //
    private fun setUpViewModel() {
        val application = requireNotNull(this).application
        val database = ListsRoomDatabase.getInstance(application)
        val repository = ListsRepository.getInstance(database)
        val viewModelFactory = ListsViewModelFactory(repository, application)
        listsViewModel = ViewModelProvider(
                this, viewModelFactory).get(ListsViewModel::class.java)
    }

    private fun setUpAppBar() {
        val moreOptionsDrawable = R.drawable.ic_more_options_24dp
        binding?.homeScreenTopAppbar?.title = "House Mate"
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            binding?.homeScreenTopAppbar?.overflowIcon = ContextCompat.getDrawable(this, moreOptionsDrawable) // might have to do this in every activity.
        }
        binding?.homeScreenTopAppbar?.setNavigationOnClickListener {
            //todo: handle navigation icon press
            //the navigation icon is the icon to the left
            // command+f 'Navigation icon attributes' in material design website
        }

        binding?.homeScreenTopAppbar?.setOnMenuItemClickListener { menuItem ->
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

    private fun setUpTabs() {
        binding?.listsViewPager?.adapter = ViewPagerFragmentAdapter(this)

        // attaching tab mediator
        // tab mediator will synchronize the ViewPager2's position with the selected tab when a tab is selected.
        binding?.let { TabLayoutMediator(binding!!.mainActivityTabLayout, it.listsViewPager) {
                tab: TabLayout.Tab, position: Int ->
                tab.text = tabTitles[position]
            }.attach()
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

    // HELPER FUNCTIONS //

    // Adapter for the viewPager2 (Inner Class) //
    private inner class ViewPagerFragmentAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return ShoppingListFragment()
                1 -> return ChoresListFragment()
            }
            return ShoppingListFragment()
        }

        override fun getItemCount(): Int {
            return tabTitles.size
        }
    }
}
