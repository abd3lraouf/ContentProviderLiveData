package me.abdelraoufsabri.contacts

import android.Manifest.permission.READ_CONTACTS
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import me.abdelraoufsabri.contacts.data.ContactViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var contactAdapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!isPermissionGranted())
            showPermissionDialog()
        else {
            setupRecyclerView()
        }
    }

    private fun setupRecyclerView() {
        contactAdapter = ContactAdapter()
        with(rvContactList) {
            adapter = contactAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )
        }


        setContacts()
    }

    private fun setContacts() {
        ViewModelProvider(this)
            .get(ContactViewModel::class.java)
            .contacts.observe(this, Observer { contacts ->
                with(contacts) {
                    tvNoContacts.visibility = if (isNotEmpty()) View.GONE else View.VISIBLE
                    contactAdapter.contacts = this
                }
            })
    }

    private fun isPermissionGranted(): Boolean =
        ContextCompat.checkSelfPermission(this, READ_CONTACTS) == PackageManager.PERMISSION_GRANTED

    private fun showPermissionDialog() {
        AlertDialog.Builder(this)
            .apply {
                setTitle("Contact Permission required!")
                setMessage("Contact permission is required to show contacts.")
                setPositiveButton(android.R.string.ok) { _, _ -> initiatePermission() }
                setOnDismissListener { }
            }.create().show()

    }

    private fun permissionDeniedDialog() {
        AlertDialog.Builder(this)
            .apply {
                setTitle("Contact Permission required!")
                setMessage("Cannot show contacts list without contacts read permission.")
                setPositiveButton(android.R.string.ok) { _, _ -> }
                setOnDismissListener { }
            }.create().show()
    }

    private fun initiatePermission() {
        ActivityCompat.requestPermissions(this, arrayOf(READ_CONTACTS), PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    setupRecyclerView()
                else permissionDeniedDialog()
            }
            else -> permissionDeniedDialog()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 666
    }
}