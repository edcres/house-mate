package com.aldreduser.housemate.ui.main.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aldreduser.housemate.R
import com.aldreduser.housemate.ui.main.adapter.ShoppingListAdapter
import com.aldreduser.housemate.ui.main.viewmodels.lists.ShoppingListViewModel
import kotlinx.android.synthetic.main.activity_main.*

// storage
//local
// todo: make room with a view database first (i think it's only local storage)
// todo: repository
//  -get @Update and @Delete implemented throughout the app https://www.youtube.com/watch?v=5rfBU75sguk
//  -database with multiple entities   https://kirillsuslov.medium.com/how-to-add-more-that-one-entity-in-room-5cc3743219c0
// remote
// rn shopping list has remote implemented, and chores list has local implemented
//  -change repository
// todo: remote database
// todo: when getting data from remote storage, edit 'ApiServiceImpl' file

// Contextual actionbar bug
// todo: fix contextual actionbar bug. Its activates by default at the beginning of the app

// recyclerview
// todo: continue the codelab at part 11 ->    https://developer.android.com/codelabs/android-room-with-a-view-kotlin/#9
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
//https://developer.android.com/codelabs/android-databinding#0

// delete/update features
// todo: implement delete feature
// todo: implement update feature

// user
// todo: In the home activity, have the user put in his name so it is displayed in 'added by:' (saved in shared preferences)
//  user can choose anonymous
// todo: in home activity, check if user has name registered (in shared preferences), if not ask for name
// todo: make feature so that user can change their name later on

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

// Home Screen
class MainActivity : AppCompatActivity() {

    private lateinit var shoppingListViewModel: ShoppingListViewModel
    private lateinit var adapter: ShoppingListAdapter       // for RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // todo: check if user already input a name previously (from shared preferences) (have the logic in a viewmodel)
        //  if not send to shopping list activity (or last activity visited)
        // todo: ask user to put their name (so that others can see who added to do items)
        //  user can choose anon

        setUpAppBar()
        navigateToActivity() // todo: put this first after checking if user already input their name
    }

    private fun navigateToActivity () {
        // todo: pass a parameter here after checking what's the last activity the user used (shopping/chores) (shopping activity is the default)
        // navigate to shopping list activity
        val newIntent = Intent(this, ShoppingListActivity::class.java)
        startActivity(newIntent)
    }

    private fun setUpAppBar() {
        homeScreenTopAppBar.title = "House Mate"
    }
}
