package com.aldreduser.housemate.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aldreduser.housemate.R
import kotlinx.android.synthetic.main.fragment_add_shopping_item.*

class AddShoppingItemFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_shopping_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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