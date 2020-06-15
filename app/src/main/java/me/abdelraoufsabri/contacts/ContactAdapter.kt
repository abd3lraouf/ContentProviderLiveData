package me.abdelraoufsabri.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_contact.view.*
import me.abdelraoufsabri.contacts.data.Contact

class ContactAdapter(
    contacts: List<Contact> = mutableListOf()
) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    var contacts = contacts
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts[position]

        holder.tvName.text = contact.name
        with(holder.ivContactImage) {
            Glide.with(context)
                .load(R.drawable.ic_account_circle_24px)
                .into(this)
        }
    }

    override fun getItemCount() = contacts.count()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.tvName
        val ivContactImage: ImageView = view.ivContactImage
    }
}