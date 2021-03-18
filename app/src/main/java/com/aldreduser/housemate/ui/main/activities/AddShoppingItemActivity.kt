package com.aldreduser.housemate.ui.main.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aldreduser.housemate.R
import kotlinx.android.synthetic.main.activity_add_shopping_item.*

class AddShoppingItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_shopping_item)
        setupAppBar()
        setupViewModel()
        fabOnClick()
    }

    private fun fabOnClick() {
        fab_add_item.setOnClickListener {
            // todo: handle add item click
        }
    }

    //todo: call this function when user clicks add FAB
    //if this doesn't work just use 'radioButton1.checked'
    private fun priorityChosen() {
        var priority:Int = 2
        var buttonChosen = choosePriorityButton.checkedRadioButtonId
        when (buttonChosen) {
            R.id.radioButton1 -> {
                //todo: handle this
                true
            }
            R.id.radioButton2 -> {
                //todo: handle this
                true
            }
            R.id.radioButton3 -> {
                //todo: handle this
                true
            }
            else -> false
        }
    }

    private fun setupAppBar() {
        //title
        addItemTopAppBar.title = "Add Shopping Item"

        //handle navigation
        addItemTopAppBar.setNavigationOnClickListener {
            // todo: handle navigation click
        }
    }

    private fun setupViewModel() {
        /* todo: bottom code is from main activity, edit it.w
        mainViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(MainViewModel::class.java)
         */
    }
}