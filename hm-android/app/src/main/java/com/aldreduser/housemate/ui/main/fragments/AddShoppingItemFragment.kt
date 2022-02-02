package com.aldreduser.housemate.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.aldreduser.housemate.R
import com.aldreduser.housemate.databinding.ActivityAddShoppingItemBinding
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel

class AddShoppingItemFragment : Fragment() {

    private val fragmentTag = "AddShopItemTAG"
    private var binding: FragmentAddShoppingItemBinding? = null
    private val listsViewModel: ListsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_shopping_item, container, false)
    }
}