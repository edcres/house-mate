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

// todo: Create a new branch for firebase persistence and get rid of Room here
// https://firebase.google.com/docs/firestore/data-model

// todo: learn about firebase and how to cache data

// todo: need to find a way to send xml widget values to the repository
// had to take out ="@=
// there is a way to do 2-way databinging with binding adapters, but i rather not
//  example: 'TwoWaySample' app -> 'interval-timer.xml' -> 'numberOfSets' property (has getter and setter)

// storage
// only have firebase with the firebase local cache. Get rid of room in this app
// todo: remote
// use firebase to store remote data (local database will be used as a cache)
// edit: api folder files, repository, viewModel/viewModelFactory
// when getting data from remote storage, edit 'ApiServiceImpl' file
// todo: local + remote
// Data in the remote database is a priority bc different users will be interacting with it.
//  - compare local database to remote database for data differences, update local database with differences
//  - if there's a conflict with the same item ask user if they want to overwrite it with his local database data
// -database with multiple entities   https://kirillsuslov.medium.com/how-to-add-more-that-one-entity-in-room-5cc3743219c0

// todo: user
// In the home activity, have the user put in his name so it is displayed in 'added by:' (saved in shared preferences)
// user can choose anonymous
// in home activity, check if user has name registered (in shared preferences), if not ask for name
// make feature so that user can change their name later on

// todo: set up all the queries

// recyclerview
// todo: contexcual actionBar: long click on recycler items to engage 'contextual actionbar' https://developer.android.com/guide/topics/ui/menus
// more options 3dots at top right of toolbar to select multiple items to delete (maybe also duplicate)
// -this is a contextual action bar, so recycler items can be selected and choose to be deleted through the actionbar
// todo: 'more options' icon pops up a select box in each recycler item, each selected has the option to be deleted or duplicated.
// alternatively: use contextual action bar to long click items, these can be deleted or duplicated

// todo: the viewModel might not be connected to the edit activities
// maybe use the app context instead of the main activity context
//  -to do this, extend 'AndroidViewModel()' instead of 'ViewModel()'

// todo: navigation
// (make sure this is good) navigation and arrow icon in all activities (except the one that opens when the app opens)
// when user backs out of adding a new item, ask if they're sure they wanna cancel.
// when user goes back in navigation from 'add shoppingList item activity', app asks to cancel adding new activity

// todo: either keep AddListItem as an activity or fragment.

// todo: clean up unused imports
// todo: take care of warnings
// todo: clean up comments

/*
Improve MVVM architecture by:
-Implement Dependency Inject Framework - Dagger in the project.
-Create base classes such as BaseActivity.
-Create Interfaces for the classes wherever optimal. (maybe)
-Write Unit-Test
*/

//future
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
        //setUpContextualAppBar() todo:
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
