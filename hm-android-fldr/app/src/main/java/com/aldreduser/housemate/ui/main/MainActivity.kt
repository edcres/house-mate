package com.aldreduser.housemate.ui.main

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.aldreduser.housemate.BottomSheetFragment
import com.aldreduser.housemate.R
import com.aldreduser.housemate.ui.main.fragments.StartFragment
import com.aldreduser.housemate.util.ListType

/** App explanation:
 *
 * The app displays a list of shopping items and a list of chores
 * The data is shared across members of the same group.
 *  - To join a group, users have to type the correct group id
 * To view more information on a list item, the user has to click the drop down button.
 */

class MainActivity : AppCompatActivity(), StartFragment.OnBottomSheetCallListener {

    private var modalBottomSheet: BottomSheetFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = ContextCompat.getColor(this, R.color.black)
        }
    }

    override fun sendItemToView(itemToView: Any, listType: ListType) {
        modalBottomSheet = BottomSheetFragment.newInstance(itemToView)
        modalBottomSheet?.show(supportFragmentManager, modalBottomSheet?.tag)
    }
}
