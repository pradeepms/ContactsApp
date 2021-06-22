package com.pacewisdom.contacts.viewmodels

import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import com.pacewisdom.contacts.data.models.Result
import kotlinx.coroutines.flow.collectIndexed

import org.junit.Before
import org.junit.Test

class ContactsRepositoryTest : TestCase() {
    private lateinit var viewModel: ContactsViewModel
    private lateinit var repository: FakeContactRepository

    @Before
    public override fun setUp() {
        super.setUp()
        repository = FakeContactRepository()
        viewModel = ContactsViewModel(repository)
    }


    @Test
    fun testFetchContacts() {
        runBlocking {
            repository.fetchContacts().collectIndexed { index, value ->
                if (index == 0) assert(value == Result.Loading)
                if (index == 1) {
                    assert(value is Result.Success)
                    val contacts = value as Result.Success
                    assert(contacts.data.size == 1)
                }
            }
        }
    }

    @Test
    fun testAddContact() {
        runBlocking {
            repository.addContact("Pradeep", "9900000000").collectIndexed { index, value ->
                if (index == 0) assert(value.peekContent() == "Success")
            }
        }
    }
}