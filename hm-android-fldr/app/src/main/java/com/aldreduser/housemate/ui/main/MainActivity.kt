package com.aldreduser.housemate.ui.main

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.aldreduser.housemate.R

// todo:
// -Make the color of the items appropriate to their urgency.

// todo: Future Improvements:
// Edit colors on dark mode
// When an urgent item is added, send a notification.
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = ContextCompat.getColor(this, R.color.black)
        }
    }
}
