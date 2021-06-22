package com.pacewisdom.contacts.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.pacewisdom.contacts.R
import com.pacewisdom.contacts.adapters.ContactsAdapter
import com.pacewisdom.contacts.data.models.Contact
import com.pacewisdom.contacts.data.models.Result
import com.pacewisdom.contacts.databinding.FragmentListContactsBinding
import com.pacewisdom.contacts.viewmodels.ContactsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListContactsFragment : BaseFragment<FragmentListContactsBinding>() {
    private val viewModel: ContactsViewModel by activityViewModels()
    private lateinit var adapter: ContactsAdapter
    private val contacts: MutableList<Contact> = mutableListOf()

    companion object {
        private const val TAG = "ListContactsFragment"
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentListContactsBinding = FragmentListContactsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ContactsAdapter(contacts)
        binding.rvContacts.adapter = adapter
        binding.rvContacts.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        viewModel.contacts.observe(viewLifecycleOwner, {
            when (it) {
                is Result.Error -> {
                    if (it.exception is SecurityException) {
                        Log.i(TAG, "onViewCreated: Need permission")
                    } else {
                        Log.e(TAG, "onViewCreated: Something went wrong")
                    }
                }
                Result.Loading -> Log.i(TAG, "onViewCreated: ")
                is Result.Success -> {
                    contacts.clear()
                    contacts.addAll(it.data)
                    adapter.notifyDataSetChanged()
                }
            }
        })

        binding.fabAddContact.setOnClickListener {
            findNavController().navigate(R.id.addContactFragment)
        }
    }
}