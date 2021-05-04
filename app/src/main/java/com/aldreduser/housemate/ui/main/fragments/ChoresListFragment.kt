package com.aldreduser.housemate.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aldreduser.housemate.R
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.ui.main.adapters.ChoresRecyclerviewListAdapter
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import kotlinx.android.synthetic.main.fragment_chores_list.*

class ChoresListFragment : Fragment() {

    private val listsViewModel: ListsViewModel by activityViewModels()
    private lateinit var adapterRecyclerview: ChoresRecyclerviewListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        setupRecyclerView()
        setupViewModel()

        fabOnClick()
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


    // Populate Recyclerview
    private fun renderList(choreItems: List<ChoresItem>) {
        adapterRecyclerview.addData(choreItems)
        adapterRecyclerview.notifyDataSetChanged()
    }
    // ViewModel
    private fun setupViewModel() {
//        // very important, declare which view-model interacts with this activity
//        listsViewModel = ViewModelProviders.of(
//            this,
//            listsViewModellFactory(ApiHelper(ApiServiceImpl()))
//        ).get(ListsViewModel::class.java)
    }
}

