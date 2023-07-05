package com.example.a1week

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.a1week.model.Contact


class ContactAdapter(private var contactClickListener: OnContactClickListener) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    private val contactList: MutableList<Contact> = mutableListOf()

    fun setData(data: List<Contact>) {
        contactList.clear()
        contactList.addAll(data)
        notifyDataSetChanged()
    }

    interface OnContactClickListener {

        fun onContactClick(contact: Contact, position: Int)
        fun onContactLongClick(contact: Contact, position: Int)
        fun onDialButtonClick(contact: Contact, position: Int)
        fun onMsgButtonClick(contact: Contact, position: Int)
    }

    fun setContactClickListener(onContactClickListener: OnContactClickListener) {
        contactClickListener = onContactClickListener
    }

    // ViewHolder class for the individual items in the RecyclerView
    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dialButton: AppCompatImageButton  = itemView.findViewById(R.id.BtnDial)
        val msgButton: AppCompatImageButton = itemView.findViewById(R.id.BtnMsg)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val contact = contactList[position]
                    contactClickListener.onContactClick(contact, position)
                }
            }

            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val contact = contactList[position]
                    contactClickListener.onContactLongClick(contact, position)
                }
                true
            }

            dialButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val contact = contactList[position]
                    contactClickListener.onDialButtonClick(contact, position)
                }
            }

            msgButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val contact = contactList[position]
                    contactClickListener.onMsgButtonClick(contact, position)
                }
            }

        }
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
            if (contact.photoUri == null) {
                findViewById<ImageView>(R.id.contact_photo_imageview).setImageResource(R.drawable.baseline_person_24)
            } else {
                findViewById<ImageView>(R.id.contact_photo_imageview).setImageURI(contact.photoUri)
            }
        }


    }

    override fun getItemCount(): Int {
        // Return the total number of items in the list
        return contactList.size
    }
}
