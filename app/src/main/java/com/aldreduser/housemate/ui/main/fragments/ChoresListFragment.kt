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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChoresListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChoresListFragment : Fragment() {

    private lateinit var choresListViewModel: ChoresListViewModel
    private lateinit var adapterRecyclerview: ChoresRecyclerviewListAdapter

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

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







    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChoresListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChoresListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}