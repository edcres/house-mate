package com.aldreduser.housemate.ui.main.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aldreduser.housemate.R
import com.aldreduser.housemate.databinding.ActivityMainBinding
import com.aldreduser.housemate.ui.main.fragments.ChoresListFragment
import com.aldreduser.housemate.ui.main.fragments.ShoppingListFragment
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel.Companion.CLIENT_ID_SP_TAG
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel.Companion.GROUP_ID_SP_TAG
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel.Companion.USER_NAME_SP_TAG
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

// storage
// todo: local
// make sure offline access to the fire-store database works
// compare local database to remote database for data differences, update local database with differences
//  - if there's a conflict with the same item maybe ask user if they want to overwrite it with his local database data
// -database with multiple entities   https://kirillsuslov.medium.com/how-to-add-more-that-one-entity-in-room-5cc3743219c0

// todo: user
// make feature so that user can change their name later on

// todo: do the date picker thing

// todo: the viewModel might not be connected to the edit activities
// maybe use the app context instead of the main activity context
//  -to do this, extend 'AndroidViewModel()' instead of 'ViewModel()'

// todo: navigation
// (make sure this is good) navigation and arrow icon in all activities (except the one that opens when the app opens)
// when user backs out of adding a new item, ask if they're sure they wanna cancel.
// when user goes back in navigation from 'add shoppingList item activity', app asks to cancel adding new activity

// todo: display the groupID someWhere (maybe have a saved list of groupIDs with nicknames)
// todo: take care of warnings
// todo: clean up comments
// todo: clean up unused imports
// todo: change colors of list screen

/*
Improve MVVM architecture by:
-Implement Dependency Inject Framework - Dagger in the project.
-Create base classes such as BaseActivity.
-Create Interfaces for the classes wherever optimal. (maybe)
-Write Unit-Test
*/

//future
// If more lists are added, make base classes
// Set icons on the material inputs
// For the cost in shopping list, have a converter so it displays the currency,
//  and get the correct currency.
// Make when neededDoneBy into a date picker

// Home Screen
class MainActivity : AppCompatActivity() {

    private val tag = "MainActivityTAG"
    private val mainSharedPrefTag = "MainActivitySP"
    private var binding: ActivityMainBinding? = null
    private lateinit var listsViewModel: ListsViewModel
    private val tabTitles = arrayOf("Shopping List", "Chores")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        listsViewModel = ViewModelProvider(this)[ListsViewModel::class.java]
        listsViewModel.sharedPrefs =
            this.getSharedPreferences(mainSharedPrefTag, Context.MODE_PRIVATE)
        binding?.apply {
            lifecycleOwner = this@MainActivity
            viewModel = listsViewModel
            addItemListFab.setOnClickListener {
                Log.d(tag, "onCreate: groupID var: ${listsViewModel.clientGroupIDCollection}")
                Log.d(tag, "onCreate: groupID SP: ${listsViewModel.getDataFromSP(GROUP_ID_SP_TAG)}")
                Log.d(tag, "onCreate: clientID var: ${listsViewModel.clientIDCollection}")
                Log.d(tag, "onCreate: clientID SP: ${listsViewModel.getDataFromSP(CLIENT_ID_SP_TAG)}")
//                listsViewModel.clearSPs()
//                addNewItem()
            }
        }
        setUpAppBar()
        setUpTabs()
        startApplication()
        // todo: delete this
        listsViewModel.vmTestVar = "MainAct"
    }

    override fun onDestroy() {
        listsViewModel.sharedPrefs = null
        binding = null
        Log.i(tag, "onDestroy: MainActivity")
        super.onDestroy()
    }

    private fun startApplication() {
        // get user name
        val userName = listsViewModel.getDataFromSP(USER_NAME_SP_TAG)
        if (userName == null) makeDialogBoxAndSetUserName()

        // set Up Database IDs And FetchData
        val currentClientGroupID = listsViewModel.getCurrentGroupID()

        if (currentClientGroupID == null) {
            makeDialogBoxAndSetGroupID()
        } else {
            listsViewModel.setClientID()
            listsViewModel.setShoppingItemsRealtime()
            listsViewModel.setChoreItemsRealtime()
        }
    }

    // CLICK LISTENERS //
    // handle fab click
    private fun addNewItem() {
        // add workout
        val goToActivity = when (listsViewModel.fragmentInView) {
            listsViewModel.listInView[0] -> {
                Intent(this, AddShoppingItemActivity::class.java)
            }
            listsViewModel.listInView[1] -> {
                Intent(this, AddChoresItemActivity::class.java)
            }
            else -> {
                // Placeholder
                Log.d(tag, "addNewItem: else was triggered")
                Intent(this, AddShoppingItemActivity::class.java)
            }
        }
        startActivity(goToActivity)
    }

    // SET UP FUNCTIONS //
    private fun setUpAppBar() {
        val moreOptionsDrawable = R.drawable.ic_more_options_24dp
        binding?.homeScreenTopAppbar?.title = "House Mate"
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            binding?.homeScreenTopAppbar?.overflowIcon =
                // might have to do this in every activity.
                ContextCompat.getDrawable(this, moreOptionsDrawable)
        }
        binding?.homeScreenTopAppbar?.setNavigationOnClickListener {
            //todo: handle navigation icon press
            //the navigation icon is the icon to the left
            // command+f 'Navigation icon attributes' in material design website
        }

        binding?.homeScreenTopAppbar?.setOnMenuItemClickListener { menuItem ->
            val shoppingListEdit = R.id.shopping_list_edit

            when (menuItem.itemId) {
                shoppingListEdit -> {
                    listsViewModel.toggleEditBtn()
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

    private fun makeDialogBoxAndSetUserName() {
        val inputDialog = MaterialAlertDialogBuilder(this)
        val customAlertDialogView = LayoutInflater.from(this)
            .inflate(R.layout.name_dialog_box, null, false)
        val inputNameDialog: EditText = customAlertDialogView.findViewById(R.id.input_name_dialog)
        inputDialog.setView(customAlertDialogView)
            .setTitle("Your user name")
            .setPositiveButton("Accept") { dialog, _ ->
                listsViewModel.userName = inputNameDialog.text.toString()
                Log.i(tag, "makeDialogBoxAndSetUserName: accept clicked " +
                        "${listsViewModel.userName}")
                listsViewModel.sendDataToSP(USER_NAME_SP_TAG, listsViewModel.userName!!)
                dialog.dismiss()
            }
            .setNegativeButton("Anonymous") { dialog, _ ->
                Log.i(tag, "makeDialogBoxAndSetUserName: negative button called")
                listsViewModel.userName = "anon"
                listsViewModel.sendDataToSP(USER_NAME_SP_TAG, listsViewModel.userName!!)
                dialog.dismiss()
            }
            .show()
    }

    private fun makeDialogBoxAndSetGroupID() {
        val inputDialog = MaterialAlertDialogBuilder(this)
        val customAlertDialogView = LayoutInflater.from(this)
            .inflate(R.layout.name_dialog_box, null, false)
        val inputNameDialog: EditText = customAlertDialogView.findViewById(R.id.input_name_dialog)
        inputDialog.setView(customAlertDialogView)
            .setTitle("Your group ID")
            .setPositiveButton("Accept") { dialog, _ ->
                listsViewModel.clientGroupIDCollection = inputNameDialog.text.toString()
                Log.i(tag, "makeDialogBoxAndSetGroupID: accept clicked " +
                            "${listsViewModel.clientGroupIDCollection}")
                listsViewModel.sendDataToSP(
                    GROUP_ID_SP_TAG,
                    listsViewModel.clientGroupIDCollection!!
                )
                listsViewModel.setShoppingItemsRealtime()
                listsViewModel.setChoreItemsRealtime()
                dialog.dismiss()
            }
            .setNegativeButton("New Group") { dialog, _ ->
                Log.i(tag, "makeDialogBoxAndSetGroupID: negative button called")
                listsViewModel.generateClientGroupID()
                dialog.dismiss()
            }
            .show()
    }

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
