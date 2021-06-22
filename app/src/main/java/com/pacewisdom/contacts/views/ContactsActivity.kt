package com.pacewisdom.contacts.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pacewisdom.contacts.databinding.ActivityContactsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}