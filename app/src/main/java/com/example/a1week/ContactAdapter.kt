package com.example.a1week

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a1week.model.Contact

class ContactAdapter : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    private val contactList: MutableList<Contact> = mutableListOf()

    fun setData(data: List<Contact>) {
        contactList.clear()
        contactList.addAll(data)
        notifyDataSetChanged()
    }

    // ViewHolder class for the individual items in the RecyclerView
    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Initialize views within the item layout if needed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_contact, parent, false
        )
        return ContactViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.itemView.apply {
            // Bind data to views within the item layout
            findViewById<TextView>(R.id.txtName).text = contact.name
            findViewById<TextView>(R.id.txtPhoneNumber).text = contact.phoneNumber
        }
    }

    override fun getItemCount(): Int {
        // Return the total number of items in the list
        return contactList.size
    }
}
