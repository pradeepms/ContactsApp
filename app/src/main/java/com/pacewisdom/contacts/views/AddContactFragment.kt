package com.pacewisdom.contacts.views

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.pacewisdom.contacts.R
import com.pacewisdom.contacts.databinding.FragmentAddContactBinding
import com.pacewisdom.contacts.utils.Constants.FAILED
import com.pacewisdom.contacts.utils.Constants.SUCCESS
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

        // On click Save button, call viewModel addContact to save contact
        binding.btnSaveContact.setOnClickListener {
            val name = binding.tilName.editText?.text.toString().trim()
            val phoneNumber = binding.tilPhoneNumber.editText?.text.toString().trim()
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phoneNumber)) {
                Toast.makeText(requireContext(), R.string.empty_input, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.addContact(name, phoneNumber)
        }

        // Observe whether the contact added successfully or not
        viewModel.isContactAdded.observe(viewLifecycleOwner, EventObserver {
            when (it) {
                FAILED -> {
                    Toast.makeText(
                        requireContext(),
                        R.string.contact_add_failed,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                SUCCESS -> {
                    Toast.makeText(
                        requireContext(),
                        R.string.contact_add_success,
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigateUp()
                    viewModel.listContacts()
                }
            }
        })
    }
}