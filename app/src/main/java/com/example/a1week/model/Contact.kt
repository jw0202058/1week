package com.example.a1week.model

//import android.content.Context
//import org.json.JSONArray
//import org.json.JSONObject
//import java.io.BufferedReader
//import java.io.IOException
//import java.io.InputStreamReader
//import java.io.OutputStreamWriter

data class Contact(
    val contactId: String,
    val name: String,
    val phoneNumber: String
)
//
//object ContactManager {
//    private const val FILE_NAME = "Contacts.json"
//
//    fun getContacts(context: Context): List<Contact> {
//        val jsonString = readJsonFile(context)
//        val contactsArray = JSONArray(jsonString)
//
//        val contacts = mutableListOf<Contact>()
//        for (i in 0 until contactsArray.length()) {
//            val contactObject = contactsArray.getJSONObject(i)
//            val id = contactObject.getString("contactId")
//            val name = contactObject.getString("name")
//            val phoneNumber = contactObject.getString("phoneNumber")
//            val contact = Contact(id, name, phoneNumber)
//            contacts.add(contact)
//        }
//        return contacts
//    }
//
//    fun deleteContact(context: Context, contact: Contact) {
//        val jsonString = readJsonFile(context)
//        val contactsArray = JSONArray(jsonString)
//
//        for (i in 0 until contactsArray.length()) {
//            val contactObject = contactsArray.getJSONObject(i)
//            val name = contactObject.getString("name")
//            val phoneNumber = contactObject.getString("phoneNumber")
//
//            if (name == contact.name && phoneNumber == contact.phoneNumber) {
//                contactsArray.remove(i)
//                break
//            }
//        }
//
//        writeJsonFile(context, contactsArray.toString())
//    }
//
//    fun addContact(context: Context, contact: Contact) {
//        val jsonString = readJsonFile(context)
//        val contactsArray = JSONArray(jsonString)
//
//        val newContactObject = JSONObject()
//        newContactObject.put("name", contact.name)
//        newContactObject.put("phoneNumber", contact.phoneNumber)
//
//        contactsArray.put(newContactObject)
//
//        writeJsonFile(context, contactsArray.toString())
//    }
//
//    private fun readJsonFile(context: Context): String {
//        var reader: BufferedReader? = null
//        var jsonString: String
//        try {
//            val fileInputStream = context.openFileInput(FILE_NAME)
//            reader = BufferedReader(InputStreamReader(fileInputStream))
//            val stringBuilder = StringBuilder()
//            var line: String?
//            while (reader.readLine().also { line = it } != null) {
//                stringBuilder.append(line)
//            }
//            jsonString = stringBuilder.toString()
//        } catch (e: IOException) {
//            jsonString = "[]"
//        } finally {
//            reader?.close()
//        }
//        return jsonString
//    }
//
//    private fun writeJsonFile(context: Context, jsonString: String) {
//        var writer: OutputStreamWriter? = null
//        try {
//            val fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
//            writer = OutputStreamWriter(fileOutputStream)
//            writer.write(jsonString)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } finally {
//            writer?.close()
//        }
//    }
//}
