package com.example.a1week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.a1week.model.Contact
import androidx.fragment.app.DialogFragment

class ContactDetailFragment : DialogFragment() {

    companion object {
        private const val ARG_CONTACT = "contact"

        fun newInstance(contact: Contact): ContactDetailFragment {
            val fragment = ContactDetailFragment()
            val args = Bundle()
            args.putParcelable(ARG_CONTACT, contact)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_detail, container, false)

        // Retrieve the contact from arguments
        val contact: Contact? = arguments?.getParcelable(ARG_CONTACT)

        // Display contact details
        contact?.let {
            val nameTextView: TextView = view.findViewById(R.id.contact_name_textview)
            val phoneNumberTextView: TextView = view.findViewById(R.id.contact_phone_number_textview)
            val profileImageView: ImageView = view.findViewById(R.id.contact_imageview)

            nameTextView.text = it.name
            phoneNumberTextView.text = it.phoneNumber
            if (contact.photoUri == null) {
                profileImageView.setImageResource(R.drawable.baseline_person_24)
            } else {
                profileImageView.setImageURI(contact.photoUri)
            }
        }

        return view
    }
}
