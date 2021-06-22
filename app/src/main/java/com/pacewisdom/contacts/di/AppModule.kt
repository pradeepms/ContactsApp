package com.pacewisdom.contacts.di

import android.app.Application
import com.pacewisdom.contacts.ContactsApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesApplication(application: Application): ContactsApplication {
        return application as ContactsApplication
    }
}