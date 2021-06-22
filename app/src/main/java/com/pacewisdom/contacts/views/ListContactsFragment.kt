package com.pacewisdom.contacts.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.pacewisdom.contacts.R
import com.pacewisdom.contacts.adapters.ContactsAdapter
import com.pacewisdom.contacts.data.models.Result
import com.pacewisdom.contacts.databinding.FragmentListContactsBinding
import com.pacewisdom.contacts.utils.EventObserver
import com.pacewisdom.contacts.viewmodels.ContactsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListContactsFragment : BaseFragment<FragmentListContactsBinding>() {
    private val viewModel: ContactsViewModel by activityViewModels()
    @Inject
    lateinit var adapter: ContactsAdapter

    companion object {
        private const val TAG = "ListContactsFragment"
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentListContactsBinding = FragmentListContactsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvContacts.adapter = adapter
        binding.rvContacts.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )

        // Observe contact list from the phone book
        viewModel.contacts.observe(viewLifecycleOwner, {
            when (it) {
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    if (it.exception is SecurityException) {
                        Toast.makeText(requireContext(), R.string.need_contact_permission, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), R.string.generic_error, Toast.LENGTH_SHORT).show()
                    }
                }
                Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(it.data)
                }
            }
        })

        viewModel.hasContactPermission.observe(viewLifecycleOwner, {
            binding.fabAddContact.isEnabled = it
        })

        binding.fabAddContact.setOnClickListener {
            findNavController().navigate(R.id.addContactFragment)
        }
    }
}