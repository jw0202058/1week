package com.example.a1week

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a1week.model.Contact
//import android.widget.ImageView
//import android.content.ContentUris
//import android.provider.MediaStore
//import android.graphics.Bitmap

class ContactFragment : Fragment(), ContactAdapter.OnContactClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ContactAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        adapter = ContactAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Check if the app has permission to access contacts
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission to access contacts
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSION_REQUEST_READ_CONTACTS
            )
        } else {
            // Permission already granted, fetch contacts
            loadContacts()
        }

        return view
    }

    override fun onContactClick(contact: Contact, position: Int) {
        // Handle item click event
        Toast.makeText(context, "Clicked on ${contact.name}", Toast.LENGTH_SHORT).show()
        // Implement your desired action for clicking a contact

//        // Retrieve the contact's photo
//        val photoUri = contact.photoUri
//        photoUri?.let { uri ->
//            val resolver: ContentResolver = requireContext().contentResolver
//            val photoBitmap = MediaStore.Images.Media.getBitmap(resolver, uri)
//            showContactPhoto(photoBitmap)
//        }
    }

    override fun onContactLongClick(contact: Contact, position: Int) {
        // Handle item long click event
        Toast.makeText(context, "Long clicked on ${contact.phoneNumber}", Toast.LENGTH_SHORT).show()
        // Implement your desired action for long clicking a contact
    }

    override fun onDialButtonClick(contact: Contact, position: Int) {
        Toast.makeText(context, "Dial to ${contact.name}", Toast.LENGTH_SHORT).show()
        val phoneNumber = contact.phoneNumber
        val phoneNumUri = Uri.parse("tel:$phoneNumber")
        val dialIntent = Intent(Intent.ACTION_DIAL, phoneNumUri)
        startActivity(dialIntent)
    }

    override fun onMsgButtonClick(contact: Contact, position: Int) {
        val phoneNumber = contact.phoneNumber
        val phoneNumUri = Uri.parse("smsto:$phoneNumber")
        val msgIntent = Intent(Intent.ACTION_SENDTO, phoneNumUri)
        msgIntent.putExtra("sms_body", "Here goes your message...")
        startActivity(msgIntent)
    }

    @SuppressLint("Range")
    private fun loadContacts() {
        val contentResolver: ContentResolver = requireContext().contentResolver

        // Query the contacts
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        cursor?.let {
            if (it.count > 0) {
                val contacts = mutableListOf<Contact>()

                while (it.moveToNext()) {
                    val id = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
                    val name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val photoUri = getContactPhotoUri(id)

                    val phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )

                    phoneCursor?.let { phoneCursor ->
                        while (phoneCursor.moveToNext()) {
                            val phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            val contact = Contact(id, name, phoneNumber, photoUri)
                            contacts.add(contact)
                        }

                        phoneCursor.close()
                    }
                }

                adapter.setData(contacts)
            }

            it.close()

        }
    }


    @SuppressLint("Range")
    private fun getContactPhotoUri(contactId: String): Uri? {
        val contentResolver: ContentResolver = requireContext().contentResolver
        val projection = arrayOf(ContactsContract.Contacts.PHOTO_URI)
        val selection = ContactsContract.Contacts._ID + " = ?"
        val selectionArgs = arrayOf(contactId)
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val photoUriString = it.getString(it.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))
                if (photoUriString != null) {
                    return Uri.parse(photoUriString)
                }
            }
        }

        return null
    }
//
//
//    private fun showContactPhoto(bitmap: Bitmap) {
//        // Display the contact photo in an ImageView
//        val imageView: ImageView = requireView().findViewById(R.id.contact_photo_imageview)
//        imageView.setImageBitmap(bitmap)
//    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_READ_CONTACTS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, fetch contacts
                loadContacts()
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_READ_CONTACTS = 1
    }
}
