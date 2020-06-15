package me.abdelraoufsabri.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.abdelraoufsabri.contacts.data.ContactViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var contactAdapter: ContactAdapter
    private lateinit var nothingToDisplay: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        nothingToDisplay = findViewById(R.id.text_no_result)

        if (!isPermissionGranted())
            showPermissionDialog()
        else {
            setupRecyclerView()
        }
    }

    private fun setupRecyclerView() {
        contactAdapter = ContactAdapter()
        recyclerView.adapter = contactAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val divider = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(divider)

        setContacts()
    }

    private fun setContacts() {
        ViewModelProvider(this)
            .get(ContactViewModel::class.java)
            .contacts.observe(this, Observer { contacts ->
                with(contacts) {
                    nothingToDisplay.visibility = if (isNotEmpty()) View.GONE else View.VISIBLE
                    contactAdapter.contacts = this
                }
            })
    }

    private fun isPermissionGranted(): Boolean = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_CONTACTS
    ) == PackageManager.PERMISSION_GRANTED

    private fun showPermissionDialog() {
        val alertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Contact Permission required!")
                setMessage("Contact permission is required to show contacts.")
                setPositiveButton(android.R.string.ok) { _, _ -> initiatePermission() }
                setOnDismissListener { }
            }
            builder.create()
        }
        alertDialog.show()
    }

    private fun permissionDeniedDialog() {
        val alertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Contact Permission required!")
                setMessage("Cannot show contacts list without contacts read permission.")
                setPositiveButton(android.R.string.ok) { _, _ -> }
                setOnDismissListener { }
            }
            builder.create()
        }
        alertDialog.show()
    }

    private fun initiatePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_CONTACTS),
            PERMISSION_REQUEST_CODE
        )
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