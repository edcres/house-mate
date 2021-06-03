package com.aldreduser.housemate.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.aldreduser.housemate.data.ListsRepository
import com.aldreduser.housemate.data.model.room.ListsRoomDatabase
import com.aldreduser.housemate.databinding.FragmentAddListItemBinding
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModelFactory

// todo: make this an addItem fragment (works for shopping or chore items)
//  -widgets are hidden depending on which list is being manipulated
//  -or have 1 fragment but 2 layout files that contain the inputs for each list respectively
class AddListItemFragment : Fragment() {

    private var binding: FragmentAddListItemBinding? = null
    private lateinit var listsViewModel: ListsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentAddListItemBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        setUpViewModel()
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner     //used so that the binding can observe LiveData updates
            viewModel = listsViewModel
            fabAddItem.setOnClickListener { fabOnClick() }
        }
        setupAppBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    // CLICK HANDLERS //

    private fun fabOnClick() {
        // todo: handle add item click
    }

    // SET UP FUNCTIONS //
    private fun setUpViewModel() {
        val application = requireNotNull(this.activity).application
        val database = ListsRoomDatabase.getInstance(application)
        val repository = ListsRepository.getInstance(database)
        val viewModelFactory = ListsViewModelFactory(repository, application)
        listsViewModel = ViewModelProvider(
            this, viewModelFactory).get(ListsViewModel::class.java)
    }

    private fun setupAppBar() {
        //title
        binding?.addItemTopAppbar?.title = "Add Shopping Item"

        //handle navigation
        binding?.addItemTopAppbar?.setNavigationOnClickListener {
            // todo: handle navigation click
        }
    }

    // HELPER FUNCTIONS //

    //todo: call this function when user clicks add FAB
    //if this doesn't work just use 'radioButton1.checked'
    private fun priorityChosen() {
//
//        var priority:Int = 2
//        var buttonChosen = binding?.choosePriorityButton?.checkedRadioButtonId
//        when (buttonChosen) {
//            binding?.radioButton1?.id -> {
//                //todo: handle this
//                true
//            }
//            binding?.radioButton2?.id -> {
//                //todo: handle this
//                true
//            }
//            binding?.radioButton3?.id -> {
//                //todo: handle this
//                true
//            }
//            else -> false
//        }
    }
}