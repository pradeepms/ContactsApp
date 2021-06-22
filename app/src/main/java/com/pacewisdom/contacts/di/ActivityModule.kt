package com.pacewisdom.contacts.di

import android.app.Application
import com.pacewisdom.contacts.adapters.ContactsAdapter
import com.pacewisdom.contacts.data.repositories.ContactsRepository
import com.pacewisdom.contacts.data.repositories.ContactsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object ActivityModule {

    @Provides
    fun providesContactRepository(application: Application): ContactsRepository {
        return ContactsRepositoryImpl(application)
    }

    @Provides
    fun providesContactAdapter(): ContactsAdapter {
        return ContactsAdapter()
    }
}