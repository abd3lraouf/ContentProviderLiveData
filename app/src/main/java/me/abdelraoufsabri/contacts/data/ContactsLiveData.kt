package me.abdelraoufsabri.contacts.data

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract


class ContactsLiveData(
    private val context: Context
) : ContentProviderLiveData<List<Contact>>(context, URI) {
    companion object {
        val URI: Uri = ContactsContract.Contacts.CONTENT_URI
        const val ID = ContactsContract.Contacts._ID
        const val DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME
    }

    override fun getContentProviderValue(): List<Contact> {
        val projection = arrayOf(ID, DISPLAY_NAME)

        return context.contentResolver
            .query(URI, projection, null, null, DISPLAY_NAME)
            .use {
                it!!
                generateSequence {
                    it.moveToNext()
                    Contact(it.getLong(0), it.getString(1))
                }.take(it.count).toList()
            }
    }
}