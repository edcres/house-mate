package com.aldreduser.housemate.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aldreduser.housemate.R
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.ui.main.adapters.ChoresRecyclerviewListAdapter
import com.aldreduser.housemate.ui.main.viewmodels.lists.ChoresListViewModel
import com.aldreduser.housemate.util.Status
import kotlinx.android.synthetic.main.fragment_chores_list.*

class ChoresListFragment : Fragment() {

    private lateinit var choresListViewModel: ChoresListViewModel
    private lateinit var adapterRecyclerview: ChoresRecyclerviewListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupRecyclerView()
        setupObserver()
        setupViewModel()

        fabOnClick()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chores_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun fabOnClick() {
        // todo: pass some data to that activity
        // navigate to the  addChoreList item activity
        choresListFab.setOnClickListener() {

            //todo: navigate to: AddChoreItemActivity
        }
    }

    // RecyclerView
    private fun setupRecyclerView() {
        // populate recyclerview
        choresListRecyclerView.layoutManager = LinearLayoutManager(context)  //todo: possible bug: 'context' was 'this'
        adapterRecyclerview = ChoresRecyclerviewListAdapter(arrayListOf())

        choresListRecyclerView.addItemDecoration(
            DividerItemDecoration(
                choresListRecyclerView.context,
                (choresListRecyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        choresListRecyclerView.adapter = adapterRecyclerview
    }

    // Observer
    private fun setupObserver() {
        choresListViewModel.getChoreItems().observe(this, Observer {
            when(it.status) {
                Status.SUCCESS -> {
                    // when status is Success: hide bar
                    it.data?.let { choreItems -> renderList(choreItems) }
                    choresListRecyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    // when status is Loading: show progress bar
                    choresListRecyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    // handle error (idk if the error is already handled or not)
                    // when status is error: hide progress bar
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    // Populate Recyclerview
    private fun renderList(choreItems: List<ChoresItem>) {
        adapterRecyclerview.addData(choreItems)
        adapterRecyclerview.notifyDataSetChanged()
    }
    // ViewModel
    private fun setupViewModel() {
//        // very important, declare which view-model interacts with this activity
//        choresListViewModel = ViewModelProviders.of(
//            this,
//            ChoresListViewModelFactory(ApiHelper(ApiServiceImpl()))
//        ).get(ChoresListViewModel::class.java)
    }
}