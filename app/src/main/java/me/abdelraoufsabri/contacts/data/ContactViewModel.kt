package me.abdelraoufsabri.contacts.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class ContactViewModel(
    application: Application
) : AndroidViewModel(application) {
    val contacts = ContactsLiveData(application.applicationContext)
}