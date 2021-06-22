package com.pacewisdom.contacts.viewmodels

import com.pacewisdom.contacts.data.models.Contact
import com.pacewisdom.contacts.data.models.Result
import com.pacewisdom.contacts.data.repositories.ContactsRepository
import com.pacewisdom.contacts.utils.Event
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeContactRepository : ContactsRepository {
    override suspend fun fetchContacts(): Flow<Result<List<Contact>>> {
        return flow {
            emit(Result.Loading)
            // Fake contact call
            delay(1000)
            // Send a random contact data
            val contacts = mutableListOf(Contact("Test", "9900000000"))
            emit(Result.Success(contacts))
        }
    }

    override suspend fun addContact(name: String, phoneNumber: String): Flow<Event<String>> {
        return flow {
            // Fake contact call
            delay(1000)
            emit(Event("Success"))
        }
    }
}