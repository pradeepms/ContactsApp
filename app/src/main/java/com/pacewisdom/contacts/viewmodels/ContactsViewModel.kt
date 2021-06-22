package com.pacewisdom.contacts.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pacewisdom.contacts.data.models.Contact
import com.pacewisdom.contacts.data.repositories.ContactsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import com.pacewisdom.contacts.data.models.Result
import com.pacewisdom.contacts.data.repositories.ContactsRepository
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

    // Holders list of fetched contacts from the phone book
    private val _contacts = MutableLiveData<Result<List<Contact>>>()
    val contacts = _contacts

    // Event listener to handle connect added state
    private val _isContactAdded = MutableLiveData<Event<String>>()
    val isContactAdded = _isContactAdded

    // Flag to handle add contact button enabled state
    private val _hasContactPermission = MutableLiveData<Boolean>()
    val hasContactPermission = _hasContactPermission

    // Fetching contacts list in the background thread
    fun listContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            contactsRepository.fetchContacts().collect {
                _contacts.postValue(it)
            }
        }
    }

    // Adding contact
    fun addContact(name: String, phoneNumber: String) {
        viewModelScope.launch(Dispatchers.IO) {
            contactsRepository.addContact(name, phoneNumber).collect {
                _isContactAdded.postValue(it)
            }
        }
    }

    fun setContactPermission(hasPermission: Boolean) {
        _hasContactPermission.value = hasPermission
    }
}