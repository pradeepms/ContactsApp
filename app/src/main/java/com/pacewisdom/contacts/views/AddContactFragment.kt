package com.pacewisdom.contacts.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.pacewisdom.contacts.data.models.Result
import com.pacewisdom.contacts.databinding.FragmentAddContactBinding
import com.pacewisdom.contacts.utils.EventObserver
import com.pacewisdom.contacts.viewmodels.ContactsViewModel

class AddContactFragment : BaseFragment<FragmentAddContactBinding>() {
    private val viewModel: ContactsViewModel by activityViewModels()

    companion object {
        private const val TAG = "AddContactFragment"
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddContactBinding = FragmentAddContactBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.materialToolbar.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnSaveContact.setOnClickListener {
            viewModel.addContact(
                binding.tilName.editText?.text.toString().trim(),
                binding.tilPhoneNumber.editText?.text.toString().trim()
            )
        }
        viewModel.isContactAdded.observe(viewLifecycleOwner, EventObserver {
            when (it) {
                "Failed" -> {
                    Toast.makeText(
                        requireContext(),
                        "Failed!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                "Success" -> {
                    Toast.makeText(
                        requireContext(),
                        "Contact added successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigateUp()
                    viewModel.listContacts()
                }
            }
        })
    }
}