# Content Provider with LiveData and ViewModel

original repo: [absolute-z3ro/Contacts](https://github.com/absolute-z3ro/Contacts "absolute-z3ro/Contacts")

Content provider implementation with livedata and viewmodel

## Sample

<div align="center">
  <a href="https://www.youtube.com/watch?v=9wCQAIakmnY"><img src="https://img.youtube.com/vi/9wCQAIakmnY/0.jpg" alt="Sample Video"></a>
</div>



## Code snippets:

### Usage in Activity
```kotlin
ViewModelProvider(this)
    .get(ContactViewModel::class.java)
    .contacts.observe(this, Observer { contacts ->
        with(contacts) {
            tvNoContacts.visibility = if (isNotEmpty()) View.GONE else View.VISIBLE
            contactAdapter.contacts = this
        }
    })
```

### ViewModel
```kotlin
class ContactViewModel(
    application: Application
) : AndroidViewModel(application) {
    val contacts = ContactsLiveData(application.applicationContext)
}
```

### LiveData
```kotlin
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
```
