package com.pacewisdom.contacts.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pacewisdom.contacts.data.models.Contact
import com.pacewisdom.contacts.databinding.RowContactBinding

class ContactsAdapter : ListAdapter<Contact, ContactsAdapter.ViewHolder>(ContactsDiffCallback()) {

    class ViewHolder(val binding: RowContactBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                Log.d(TAG, ": $adapterPosition")

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = getItem(position)
        holder.binding.tvName.text = contact.name
        holder.binding.tvPhoneNumber.text = contact.phoneNumber
    }

    companion object {
        private const val TAG = "ContactsAdapter"
    }
}

class ContactsDiffCallback : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean =
        oldItem.phoneNumber == newItem.phoneNumber

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean =
        oldItem == newItem

}