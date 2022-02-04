package com.aldreduser.housemate.ui.main.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aldreduser.housemate.R
import com.aldreduser.housemate.data.model.firestore.DbApiService
import com.aldreduser.housemate.databinding.FragmentStartBinding
import com.aldreduser.housemate.ui.main.MainActivity
import com.aldreduser.housemate.ui.main.fragments.nestedfragments.ChoresListFragment
import com.aldreduser.housemate.ui.main.fragments.nestedfragments.ShoppingListFragment
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class StartFragment : Fragment() {

    private val fragmentTag = "StartFragmentTAG"
    private val mainSharedPrefTag = "MainSPTAG"
    private var binding: FragmentStartBinding? = null
    private val listsViewModel: ListsViewModel by activityViewModels()
    private val tabTitles = listOf("Shopping List", "Chores")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentStartBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listsViewModel.sharedPrefs =
            this.requireActivity().getSharedPreferences(mainSharedPrefTag, Context.MODE_PRIVATE)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            addItemListFab.setOnClickListener {
                addNewItem()
//                listsViewModel.clearSPs()
            }
        }
        setUpAppBar()
        setUpTabs()
        startApplication()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listsViewModel.sharedPrefs = null
        binding = null
        Log.i(fragmentTag, "onViewCreated: StartFragment")
    }

    private fun startApplication() {
        // get user name
        listsViewModel.userName = listsViewModel.getDataFromSP(ListsViewModel.USER_NAME_SP_TAG)
        if (listsViewModel.userName == null) makeDialogBoxAndSetUserName()
        // set Up Database IDs And FetchData
        val currentClientGroupID = listsViewModel.getCurrentGroupID()
        if (currentClientGroupID == null) {
            makeDialogBoxAndSetGroupID()
        } else {
            listsViewModel.setClientID()
            listsViewModel.setShoppingItemsRealtime()
            listsViewModel.setChoreItemsRealtime()
        }
    }

    // CLICK LISTENERS //
    // handle fab click
    private fun addNewItem() {
        // add workout
        val navController = Navigation.findNavController(requireParentFragment().requireView())
        val navAction = when (listsViewModel.fragmentInView) {
            listsViewModel.listInView[0] -> {
                R.id.action_startFragment_to_addShoppingItemFragment
            }
            listsViewModel.listInView[1] -> {
                R.id.action_startFragment_to_addChoresItemFragment
            }
            else -> {
                // Placeholder
                Log.d(tag, "addNewItem: else was triggered")
                R.id.action_startFragment_to_addShoppingItemFragment
            }
        }
        navController.navigate(navAction)
    }

    // SET UP FUNCTIONS //
    private fun setUpAppBar() {
        val moreOptionsDrawable = R.drawable.ic_more_options_24dp
        binding?.homeScreenTopAppbar?.title = "House Mate"
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            binding?.homeScreenTopAppbar?.overflowIcon =
                    // might have to do this in every activity.
                ContextCompat.getDrawable(requireContext(), moreOptionsDrawable)
        }
        binding?.homeScreenTopAppbar?.setNavigationOnClickListener {
            //todo: handle navigation icon press
            //the navigation icon is the icon to the left
            // command+f 'Navigation icon attributes' in material design website
        }

        binding?.homeScreenTopAppbar?.setOnMenuItemClickListener { menuItem ->
            val shoppingListEdit = R.id.shopping_list_edit
            when (menuItem.itemId) {
                shoppingListEdit -> {
                    Log.d(fragmentTag, "editBtn clicked")
                    listsViewModel.toggleEditBtn()
                    true
                }
                else -> false
            }
        }
    }

    private fun setUpTabs() {
        binding?.listsViewPager?.adapter = ViewPagerFragmentAdapter(this.requireActivity())

        // attaching tab mediator
        // tab mediator will synchronize the ViewPager2's position with the selected tab when a tab is selected.
        binding?.let { TabLayoutMediator(binding!!.startFragmentTabLayout, it.listsViewPager) {
                tab: TabLayout.Tab, position: Int ->
            tab.text = tabTitles[position]
        }.attach()
        }
    }

    private fun makeDialogBoxAndSetUserName() {
        val inputDialog = MaterialAlertDialogBuilder(requireContext())
        val customAlertDialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.name_dialog_box, null, false)
        val inputNameDialog: EditText = customAlertDialogView.findViewById(R.id.input_name_dialog)
        inputDialog.setView(customAlertDialogView)
            .setTitle("Your user name")
            .setPositiveButton("Accept") { dialog, _ ->
                listsViewModel.userName = inputNameDialog.text.toString()
                Log.i(tag, "makeDialogBoxAndSetUserName: accept clicked " +
                        "${listsViewModel.userName}")
                listsViewModel.sendDataToSP(ListsViewModel.USER_NAME_SP_TAG, listsViewModel.userName!!)
                dialog.dismiss()
            }
            .setNegativeButton("Anonymous") { dialog, _ ->
                Log.i(tag, "makeDialogBoxAndSetUserName: negative button called")
                listsViewModel.userName = "anon"
                listsViewModel.sendDataToSP(ListsViewModel.USER_NAME_SP_TAG, listsViewModel.userName!!)
                dialog.dismiss()
            }
            .show()
    }

    private fun makeDialogBoxAndSetGroupID() {
        val inputDialog = MaterialAlertDialogBuilder(requireContext())
        val customAlertDialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.name_dialog_box, null, false)
        val inputNameDialog: EditText = customAlertDialogView.findViewById(R.id.input_name_dialog)
        inputDialog.setView(customAlertDialogView)
            .setTitle("Your group ID")
            .setPositiveButton("Accept") { dialog, _ ->
                listsViewModel.clientGroupIDCollection = inputNameDialog.text.toString()
                Log.i(tag, "makeDialogBoxAndSetGroupID: accept clicked " +
                        "${listsViewModel.clientGroupIDCollection}")
                listsViewModel.sendDataToSP(
                    ListsViewModel.GROUP_ID_SP_TAG,
                    listsViewModel.clientGroupIDCollection!!
                )
                listsViewModel.setShoppingItemsRealtime()
                listsViewModel.setChoreItemsRealtime()
                dialog.dismiss()
            }
            .setNegativeButton("New Group") { dialog, _ ->
                Log.i(tag, "makeDialogBoxAndSetGroupID: negative button called")
                listsViewModel.generateClientGroupID()
                dialog.dismiss()
            }
            .show()
    }

    // Adapter for the viewPager2 (Inner Class) //
    private inner class ViewPagerFragmentAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return ShoppingListFragment()
                1 -> return ChoresListFragment()
            }
            return ShoppingListFragment()
        }

        override fun getItemCount(): Int {
            return tabTitles.size
        }
    }
}
