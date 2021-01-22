package com.aldreduser.housemate.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aldreduser.housemate.R
import com.aldreduser.housemate.data.api.ApiHelper
import com.aldreduser.housemate.data.api.ApiServiceImpl
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.ui.base.ViewModelFactory
import com.aldreduser.housemate.ui.main.adapter.MainAdapter
import com.aldreduser.housemate.ui.main.viewmodel.MainViewModel
import com.aldreduser.housemate.utils.Status
import kotlinx.android.synthetic.main.activity_main.*

// https://blog.mindorks.com/mvvm-architecture-android-tutorial-for-beginners-step-by-step-guide

// todo: change recycler item UI, according to my own design
// todo: In the ApiServiceImpl class in the api package. There's a placeholder website to get the data,
//  CHANGE where to get the data, and get it from my own remote storage.
// todo: I'm gonna add the things for the chore item later, that way I can follow up with the example better.
// todo: change the main activity UI, make it more tailored to my app

// todo: when i start implementing chore items, command+f 'ShoppingItem' throughout the project

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: MainAdapter       // for RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUI()
        setupViewModel()
        setupObserver()
    }

    // UI
    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(arrayListOf())

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
    }

    // observe data
    private fun setupObserver() {
        mainViewModel.getShoppingItems().observe(this, Observer {
            when(it.status) {
                Status.SUCCESS -> {
                    // when status is Success: hide bar
                    progressBar.visibility = View.GONE
                    it.data?.let { shoppingItems -> renderList(shoppingItems) }
                    recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    // when status is Loading: show progress bar
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    // handle error (idk if the error is already handled or not)
                    // when status is error: hide progress bar
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT)

                }
            }
        })
    }

    // put items in the recyclerview
    private fun renderList(shoppingItems: List<ShoppingItem>) {
        adapter.addData(shoppingItems)
        adapter.notifyDataSetChanged()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(MainViewModel::class.java)

    }

}
