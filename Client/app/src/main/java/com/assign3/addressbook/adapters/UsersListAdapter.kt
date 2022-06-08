package com.assign3.addressbook.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.assign3.addressbook.R
import com.assign3.addressbook.activities.UserDetailsActivity
import com.assign3.addressbook.models.User

class UsersListAdapter(var mUsers: List<User> = ArrayList(), var userName: String = "") : RecyclerView.Adapter<UsersListAdapter.ViewHolder>() {
    private lateinit var context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.userName)
        val detailsButton: Button = itemView.findViewById(R.id.detailsButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersListAdapter.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)

        val locationView = inflater.inflate(R.layout.item_users_list, parent, false)
        return ViewHolder(locationView)
    }

    override fun onBindViewHolder(viewHolder: UsersListAdapter.ViewHolder, position: Int) {
        val user: User = mUsers[position]

        val nameTextView = viewHolder.nameTextView
        nameTextView.text = user.name

        val detailsButton = viewHolder.detailsButton
        detailsButton.setOnClickListener {
            val intent = Intent(context, UserDetailsActivity::class.java)
            intent.putExtra("Username", userName)
            intent.putExtra("UsernameDetails", user.name)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }
}