package com.aldreduser.housemate.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aldreduser.housemate.R

// https://blog.mindorks.com/mvvm-architecture-android-tutorial-for-beginners-step-by-step-guide

// todo: In the ApiServiceImpl class in the api package. There's a placeholder website to get the data,
//  CHANGE where to get the data, and get it from my own remote storage.
// todo: I'm gonna add the things for the chore item later, that way I can follow up with the example better.

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
