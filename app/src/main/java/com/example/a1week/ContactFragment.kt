package com.example.a1week

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a1week.model.Contact
import org.json.JSONObject

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

        // Read and parse the JSON file
        val jsonString = context?.assets?.open("Contacts.json")?.bufferedReader().use {
            it?.readText()
        }
        val jsonObject = JSONObject(jsonString)
        val contactsArray = jsonObject.getJSONArray("contacts")

        // Convert JSON objects to Contact objects
        val contacts = mutableListOf<Contact>()
        for (i in 0 until contactsArray.length()) {
            val contactObject = contactsArray.getJSONObject(i)
            val name = contactObject.getString("name")
            val phoneNumber = contactObject.getString("phoneNumber")
            val contact = Contact(name, phoneNumber)
            contacts.add(contact)
        }
        adapter.setContactClickListener(this)
        adapter.setData(contacts)

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the click listener to the adapter
        adapter.setContactClickListener(this)
    }
    override fun onContactClick(contact: Contact, position: Int) {
        // Handle item click event
        Toast.makeText(context, "Clicked on ${contact.name}", Toast.LENGTH_SHORT).show()
        val phoneNumber = contact.phoneNumber
        val phoneNumUri = Uri.parse("tel:${phoneNumber}")
        val dialIntent = Intent(Intent.ACTION_DIAL, phoneNumUri)
        startActivity(dialIntent)
    }

    override fun onContactLongClick(contact: Contact, position: Int) {
        // Handle item long click event
        Toast.makeText(context, "Long clicked on ${contact.phoneNumber}", Toast.LENGTH_SHORT).show()
    }
}