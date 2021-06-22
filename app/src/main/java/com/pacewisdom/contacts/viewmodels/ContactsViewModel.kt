package com.pacewisdom.contacts.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pacewisdom.contacts.data.models.Contact
import com.pacewisdom.contacts.data.repositories.ContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import com.pacewisdom.contacts.data.models.Result
import com.pacewisdom.contacts.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository
) : ViewModel() {
    companion object {
        private const val TAG = "ContactsViewModel"
    }

    private val _contacts = MutableLiveData<Result<List<Contact>>>()
    val contacts = _contacts
    private val _isContactAdded = MutableLiveData<Event<String>>()
    val isContactAdded = _isContactAdded

    fun listContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            contactsRepository.fetchContacts().collect {
                _contacts.postValue(it)
            }
        }
    }

    fun addContact(name: String, phoneNumber: String) {
        viewModelScope.launch(Dispatchers.IO) {
            contactsRepository.addContact(name, phoneNumber).collect {
                _isContactAdded.postValue(it)
            }
        }
    }
}