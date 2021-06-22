package com.pacewisdom.contacts.views

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.pacewisdom.contacts.R
import com.pacewisdom.contacts.databinding.ActivityContactsBinding
import com.pacewisdom.contacts.utils.Constants.REQUEST_CODE_CONTACTS
import com.pacewisdom.contacts.utils.EventObserver
import com.pacewisdom.contacts.viewmodels.ContactsViewModel
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import com.vmadalin.easypermissions.dialogs.DEFAULT_SETTINGS_REQ_CODE
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactsActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    companion object {
        private const val TAG = "ContactsActivity"
    }

    private val viewModel: ContactsViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        requestContactsPermission()
        viewModel.isContactAdded.observe(this, EventObserver {
            when (it) {
                "Failed" -> {
                    Toast.makeText(
                        this,
                        "Failed!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                "Success" -> {
                    Toast.makeText(
                        this,
                        "Contact added successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigateUp()
                    viewModel.listContacts()
                }
            }
        })
    }

    @AfterPermissionGranted(REQUEST_CODE_CONTACTS)
    private fun requestContactsPermission() {
        if (EasyPermissions.hasPermissions(
                this,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS
            )
        ) {
            viewModel.listContacts()
        } else {
            EasyPermissions.requestPermissions(
                host = this,
                rationale = getString(R.string.rationale_ask),
                requestCode = REQUEST_CODE_CONTACTS,
                perms = arrayOf(
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS
                )
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        Log.d(TAG, "onPermissionsDenied: ")
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            /* Should build our own implementation of SettingDialog to handle
             deprecated onActivityResult with registerForActivityResult*/
            SettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DEFAULT_SETTINGS_REQ_CODE) {
            requestContactsPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}