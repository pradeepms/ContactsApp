package com.pacewisdom.contacts.data.repositories

import com.pacewisdom.contacts.data.models.Contact
import com.pacewisdom.contacts.data.models.Result
import com.pacewisdom.contacts.utils.Event
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
    suspend fun fetchContacts(): Flow<Result<List<Contact>>>

    suspend fun addContact(name: String, phoneNumber: String): Flow<Event<String>>
}