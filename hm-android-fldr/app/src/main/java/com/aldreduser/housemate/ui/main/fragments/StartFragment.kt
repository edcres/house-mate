package com.aldreduser.housemate.ui.main.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aldreduser.housemate.R
import com.aldreduser.housemate.databinding.FragmentStartBinding
import com.aldreduser.housemate.ui.main.fragments.nestedfragments.ChoresListFragment
import com.aldreduser.housemate.ui.main.fragments.nestedfragments.ShoppingListFragment
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel.Companion.PAST_GROUPS_SP_TAG
import com.aldreduser.housemate.util.displayToast
import com.aldreduser.housemate.util.validateGroupId
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class StartFragment : Fragment() {

    private val fragmentTAG = "StartFragmentTAG"
    private val mainSharedPrefTag = "MainSPTAG"
    private var binding: FragmentStartBinding? = null
    private val listsViewModel: ListsViewModel by activityViewModels()

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
            addItemListFab.setOnClickListener { addNewItem() }
        }
        setUpAppBar()
        setUpTabs()
        startApplication()
        setObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listsViewModel.sharedPrefs = null
        binding = null
        Log.i(fragmentTAG, "onViewCreated: StartFragment")
    }

    private fun startApplication() {
        // Get user name
        listsViewModel.userName = listsViewModel.getDataFromSP(ListsViewModel.USER_NAME_SP_TAG)
        if (listsViewModel.userName == null) makeDialogBoxAndSetUserName()
        // Set Up Database IDs And FetchData
        val currentClientGroupID = listsViewModel.getCurrentGroupID()
        if (currentClientGroupID == null) {
            makeDialogBoxAndSetGroupID()
        } else {
            listsViewModel.setClientID()
            listsViewModel.setItemsRealtime(listsViewModel.listTypes[0])
            listsViewModel.setItemsRealtime(listsViewModel.listTypes[1])
        }
    }

    // LISTENERS //
    private fun addNewItem() {
        val navController = Navigation.findNavController(requireParentFragment().requireView())
        val navAction = when (listsViewModel.fragmentInView) {
            listsViewModel.listInView[0] -> R.id.action_startFragment_to_addShoppingItemFragment
            listsViewModel.listInView[1] -> R.id.action_startFragment_to_addChoresItemFragment
            else -> {
                // Placeholder
                Log.e(tag, "addNewItem: else was triggered")
                R.id.action_startFragment_to_addShoppingItemFragment
            }
        }
        navController.navigate(navAction)
    }
    private fun setObservers() {
        listsViewModel.itemToEdit.observe(viewLifecycleOwner) {
            val navController = Navigation.findNavController(requireParentFragment().requireView())
            val navAction = when (listsViewModel.fragmentInView) {
                listsViewModel.listInView[0] -> R.id.action_startFragment_to_addShoppingItemFragment
                listsViewModel.listInView[1] -> R.id.action_startFragment_to_addChoresItemFragment
                else -> {
                    Log.i(fragmentTAG, "itemToEdit set to null")
                }
            }
            if (listsViewModel.itemToEdit.value != null) navController.navigate(navAction)
        }
    }

    // SET UP FUNCTIONS //
    private fun setUpAppBar() {
        val moreOptionsDrawable = R.drawable.ic_more_options_24dp
        binding?.homeScreenTopAppbar?.title = "House Mate"
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            binding?.homeScreenTopAppbar?.overflowIcon =
                ContextCompat.getDrawable(requireContext(), moreOptionsDrawable)
        }
        binding?.homeScreenTopAppbar?.setOnMenuItemClickListener { menuItem ->
            val itemListEdit = R.id.item_list_edit
            val listOptionPastGroups = R.id.list_option_past_groups
            val listOptionChangeUsername = R.id.list_option_change_username
            val listOptionExitGroup = R.id.list_option_exit_group
            val listOptionCurrentGroup = R.id.list_option_current_group
            when (menuItem.itemId) {
                itemListEdit -> {
                    listsViewModel.toggleEditBtn()
                    true
                }
                listOptionPastGroups -> {
                    val pastGroups = listsViewModel.getDataFromSP(PAST_GROUPS_SP_TAG)
                    if (pastGroups != null) {
                        makeDialogBoxAndShowPastGroups(pastGroups.split("-"))
                    } else {
                        displayToast(requireContext(), "No past groups")
                    }
                    true
                }
                listOptionChangeUsername -> {
                    makeDialogBoxAndSetUserName()
                    true
                }
                listOptionExitGroup -> {
                    listsViewModel.clearLists()
                    makeDialogBoxAndSetGroupID()
                    true
                }
                listOptionCurrentGroup -> {
                    if (!listsViewModel.clientGroupIDCollection.isNullOrEmpty()) {
                        makeDialogBoxAndDisplayGroupID()
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun setUpTabs() {
        binding?.listsViewPager?.adapter = ViewPagerFragmentAdapter(this.requireActivity())
        binding?.let { TabLayoutMediator(binding!!.startFragmentTabLayout, it.listsViewPager) {
                tab: TabLayout.Tab, position: Int ->
            tab.text = listsViewModel.listTypes[position]
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
                listsViewModel
                    .sendDataToSP(ListsViewModel.USER_NAME_SP_TAG, listsViewModel.userName!!)
                dialog.dismiss()
            }
            .setNegativeButton("Anonymous") { dialog, _ ->
                Log.i(tag, "makeDialogBoxAndSetUserName: negative button called")
                listsViewModel.userName = "anon"
                listsViewModel
                    .sendDataToSP(ListsViewModel.USER_NAME_SP_TAG, listsViewModel.userName!!)
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
            .setTitle("Group ID")
            .setPositiveButton("Accept") { dialog, _ ->
                if (validateGroupId(inputNameDialog.text.toString())) {
                    listsViewModel.setGroupID(inputNameDialog.text.toString())
                    Log.i(
                        tag, "makeDialogBoxAndSetGroupID: accept clicked " +
                                "${listsViewModel.clientGroupIDCollection}"
                    )
                    dialog.dismiss()
                } else {
                    displayToast(requireContext(), "Enter a valid ID")
                    makeDialogBoxAndSetGroupID()
                }
            }
            .setNegativeButton("New Group") { dialog, _ ->
                Log.i(tag, "makeDialogBoxAndSetGroupID: negative button called")
                listsViewModel.generateClientGroupID()
                dialog.dismiss()
            }
            .show()
    }

    private fun makeDialogBoxAndShowPastGroups(pastOrdersList: List<String>) {
        val inputDialog = MaterialAlertDialogBuilder(requireContext())
        var selectedGroup: String? = null
        inputDialog
            .setSingleChoiceItems(pastOrdersList.toTypedArray(), -1) { _, which ->
                selectedGroup = pastOrdersList[which]
            }
            .setPositiveButton("Accept") { dialog, _ ->
                if (selectedGroup != null)
                    listsViewModel.setGroupID(selectedGroup!!)
                dialog.dismiss()

            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    private fun makeDialogBoxAndDisplayGroupID() {
        val inputDialog = MaterialAlertDialogBuilder(requireContext())
        inputDialog.setTitle(listsViewModel.clientGroupIDCollection)
            .setPositiveButton("Dismiss") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    // SET UP FUNCTIONS //

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
            return listsViewModel.listTypes.size
        }
    }
}
