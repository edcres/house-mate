package com.aldreduser.housemate.ui.main.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.aldreduser.housemate.R
import com.aldreduser.housemate.data.ListsRepository
import com.aldreduser.housemate.data.model.room.ListsRoomDatabase
import com.aldreduser.housemate.databinding.ActivityAddChoresItemBinding
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModelFactory

class AddChoresItemActivity : AppCompatActivity() {

    private var binding: ActivityAddChoresItemBinding? = null
    private lateinit var listsViewModel: ListsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddChoresItemBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setUpViewModel()
        binding?.apply {
            lifecycleOwner = this@AddChoresItemActivity
            viewModel = listsViewModel
            fabAddItem.setOnClickListener { fabOnClick() }
        }
        setupAppBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    // CLICK HANDLERS //
    private fun fabOnClick() {
        // todo: handle add item click
    }

    // SET UP FUNCTIONS //
    private fun setUpViewModel() {
        val application = requireNotNull(this).application
        val database = ListsRoomDatabase.getInstance(application)
        val repository = ListsRepository.getInstance(database)
        val viewModelFactory = ListsViewModelFactory(repository, application)
        listsViewModel = ViewModelProvider(
            this, viewModelFactory).get(ListsViewModel::class.java)
    }

    private fun setupAppBar() {
        //title
        binding?.addItemTopAppbar?.title = "Add Chore Item"

        //handle navigation
        binding?.addItemTopAppbar?.setNavigationOnClickListener {
            // todo: handle navigation click
        }
    }

    // HELPER FUNCTIONS //
}