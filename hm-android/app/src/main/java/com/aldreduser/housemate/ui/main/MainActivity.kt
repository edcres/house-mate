package com.aldreduser.housemate.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aldreduser.housemate.R

// todo: navigation
// (make sure this is good) navigation and arrow icon in all activities (except the one that opens when the app opens)
// when user backs out of adding a new item, ask if they're sure they wanna cancel.
// when user goes back in navigation from 'add shoppingList item activity', app asks to cancel adding new activity

// todo: bug: when a new group is added, change the UI to reflect that
// todo: change txtViews font to that of the first houseMate test
// todo: Change color of more options drop down menu.
//      Also the dialog box
//      Make tabs a little darker
// todo: edit recycler item dimensions
// todo: change colors of calendar picker

// todo: take care of warnings
// todo: clean up comments
// todo: clean up unused imports
// todo: test the app a lot

/*
Improve MVVM architecture by:
-Implement Dependency Inject Framework - Dagger in the project.
-Create base classes such as BaseActivity.
-Create Interfaces for the classes wherever optimal. (maybe)
-Write Unit-Tests
*/

// todo: Future Improvements:
// Take care of database operations appropriately, group and
//      client IDs, authentication, verification and so forth.
// More options drop down menu should show up lower
// Add nicknames to past groups
// When an item is added, the recyclerView animation is added at the top. Should be at the bottom
// User can choose the order items are added (it's alphabetical right now)
// When passing item data to viewModel db queries I can
//      just pass it in Data Objects instead of parameters.
// For the cost in shopping list, have a converter so it displays the currency,
//      and get the correct currency.
// When chore item is added travel to the chore list fragment
// When updating an item, delete that item from the database when the user adds the new item
//      (maybe check locally if these have the same name first)
// If more lists are added, make base classes

// todo: Bugs:
// Sometimes the items in Shopping List show up as the same qty as the items in Chores List
// If I navigate to chores tab and exit the group to a new group,
//      shopping list will have the same amount of items as chore list.
//      When I restart the app it goes back to normal.
// Chores fragment scrolls up too high if the shopping fragment high enough
//      The Chores List Fragment matches the height of the Shopping list fragment.
// When user types a group id that doesn't exists, it is being added to groupSP and pastGroupsSP.

// Home Screen
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
