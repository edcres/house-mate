package com.aldreduser.housemate.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.aldreduser.housemate.R
import androidx.navigation.ui.setupActionBarWithNavController

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

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
