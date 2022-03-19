package com.aldreduser.housemate.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.aldreduser.housemate.R

// todo:
// -Make the color of the items appropriate to their urgency.
// -todo: Bug: when the user is already in a group and clicks to Change Group, if he writes a custom
//          group id and clicks accept, a new group is created to the db under that custom id. The
//          group ids should only be automatically generated.

// todo: Future Improvements:
// Edit colors on dark mode
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
// Change color of more options drop down menu.
//      Also the dialog box
// Change colors of calendar picker
// If more lists are added, make base classes
// When a user adds a group and is the same as a previous one and it is saved in GROUPS_SP,
//      remove the previous instance of that group from the SP.
// Before navigateUp() from AddItemFragment, tell the user they
//      will lose their data and if they are sure.
// When sending an item to the db, make the date a timestamp/dateObject
// Edit Dark mode colors

// todo: Bugs:
// -When an urgent item is added, send a notification.
// Sometimes the items in Shopping List show up as the same qty as the items in Chores List
//  - When I navigate to Chores fragment and click the edit or the expand btn. Then navigate back
//      to the Shopping fragment, the Shopping list has the same num of items as the chores list.
//      (doesn't seem to happen the other way around)
//  - What I think is happening is the container for the recyclerview, or the recyclerview widget
//      itself is turning into the size of the smaller list when it was edited. When i click an
//      item in the the bigger list, the container is resized appropriately.
// If I navigate to chores tab and exit the group to a new group,
//      shopping list will have the same amount of items as chore list.
//      When I click the edit or expand btn it goes back to normal
// Chores fragment scrolls up too high if the shopping fragment high enough
//      The Chores List Fragment matches the height of the Shopping list fragment.
// When user types a group id that doesn't exists, it is being added to groupSP and pastGroupsSP.
// When the user scrolls up touching the textView below the recyclerView,
//      the app bar doesn't hide.

/*
Improve MVVM architecture by:
-Implement Dependency Inject Framework - Dagger in the project.
-Create base classes such as BaseActivity.
-Create Interfaces for the classes wherever optimal. (maybe)
-Write Unit-Tests
*/

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
