package com.pacewisdom.contacts.data.repositories

import android.content.ContentProviderOperation
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.*
import android.provider.ContactsContract.Data
import android.util.Log
import com.pacewisdom.contacts.data.models.Contact
import com.pacewisdom.contacts.data.models.Result
import com.pacewisdom.contacts.utils.Constants.FAILED
import com.pacewisdom.contacts.utils.Constants.SUCCESS
import com.pacewisdom.contacts.utils.Event
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ContactsRepositoryImpl
@Inject constructor(@ApplicationContext private val context: Context) : ContactsRepository {
    companion object {
        private const val TAG = "ContactsRepositoryImpl"
    }

    override suspend fun fetchContacts(): Flow<Result<List<Contact>>> {
        return flow {
            try {
                emit(Result.Loading)
                emit(Result.Success(readPhoneContacts()))
            } catch (error: Exception) {
                emit(Result.Error(error))
            }
        }
    }

    // Reads contacts from the phone book
    private fun readPhoneContacts(): MutableList<Contact> {
        Log.d(TAG, "readPhoneContacts: ")
        val list = mutableListOf<Contact>()
        val cursor: Cursor? = context.contentResolver
            .query(Phone.CONTENT_URI, null, null, null, Phone.DISPLAY_NAME + " ASC")
        if (cursor != null && cursor.count > 0) {
            var lastAddedPhoneNumber = ""
            while (cursor.moveToNext()) {
                val name =
                    cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME))
                val phone =
                    cursor.getString(cursor.getColumnIndex(Phone.NUMBER))
                val formattedNumber = phone.replace("[\\s\\-()]".toRegex(), "")
                if (lastAddedPhoneNumber == formattedNumber) {
                    continue
                }
                val contact = Contact(name, formattedNumber)
                list.add(contact)
                lastAddedPhoneNumber = formattedNumber
            }
        }
        cursor?.close()
        return list
    }

    // Adds new contact to the phone book
    override suspend fun addContact(name: String, phoneNumber: String): Flow<Event<String>> {
        return flow {
            Log.i(TAG, "addContact: $name $phoneNumber")
            val operationList = ArrayList<ContentProviderOperation>()
            operationList.add(
                ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build()
            )

            // Add name
            operationList.add(
                ContentProviderOperation.newInsert(Data.CONTENT_URI)
                    .withValueBackReference(Data.RAW_CONTACT_ID, 0)
                    .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(StructuredName.GIVEN_NAME, name)
                    .build()
            )

            // Add phone number
            operationList.add(
                ContentProviderOperation.newInsert(Data.CONTENT_URI)
                    .withValueBackReference(Data.RAW_CONTACT_ID, 0)
                    .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                    .withValue(Phone.NUMBER, phoneNumber)
                    .withValue(Phone.TYPE, Phone.TYPE_MOBILE)
                    .build()
            )

            try {
                val results =
                    context.contentResolver.applyBatch(ContactsContract.AUTHORITY, operationList)
                emit(Event(SUCCESS))
            } catch (e: Exception) {
                emit(Event(FAILED))
            }
        }
    }

}