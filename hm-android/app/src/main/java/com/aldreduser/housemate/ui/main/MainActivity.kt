package com.aldreduser.housemate.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aldreduser.housemate.R

// storage
// todo: local
// make sure offline access to the fire-store database works
// compare local database to remote database for data differences, update local database with differences
//  - if there's a conflict with the same item maybe ask user if they want to overwrite it with his local database data
// -database with multiple entities   https://kirillsuslov.medium.com/how-to-add-more-that-one-entity-in-room-5cc3743219c0

// todo: do the date picker thing

// todo: navigation
// (make sure this is good) navigation and arrow icon in all activities (except the one that opens when the app opens)
// when user backs out of adding a new item, ask if they're sure they wanna cancel.
// when user goes back in navigation from 'add shoppingList item activity', app asks to cancel adding new activity

// todo: display the groupID someWhere (maybe have a saved list of groupIDs with nicknames)
// todo: Change color of more options drop down menu.
//      Also the dialog box
//      Make tabs a little darker
// todo: change colors of recycler item
// todo: edit recycler item dimensions
// todo: take care of warnings
// todo: clean up comments
// todo: clean up unused imports
// todo: When chore item is added travel to the chore list fragment

/*
Improve MVVM architecture by:
-Implement Dependency Inject Framework - Dagger in the project.
-Create base classes such as BaseActivity.
-Create Interfaces for the classes wherever optimal. (maybe)
-Write Unit-Test
*/

// todo: Future Improvements:
// More options drop down menu should show up lower
// Add nicknames to past groups
// When an item is added, the recyclerView animation is added at the top. Should be at the bottom
// User can choose the order items are added (it's alphabetical right now)
// When passing item data to viewModel db queries I can
//      just pass it in Data Objects instead of parameters.
// For the cost in shopping list, have a converter so it displays the currency,
//      and get the correct currency.
// If more lists are added, make base classes

// todo: Bugs:
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
