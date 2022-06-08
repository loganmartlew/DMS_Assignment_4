package com.assign3.addressbook.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.assign3.addressbook.R
import com.assign3.addressbook.models.User

class UsersListAdapter(var mUsers: List<User> = ArrayList()) : RecyclerView.Adapter<UsersListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.userName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersListAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val locationView = inflater.inflate(R.layout.user_list_item, parent, false)
        return ViewHolder(locationView)
    }

    override fun onBindViewHolder(viewHolder: UsersListAdapter.ViewHolder, position: Int) {
        val user: User = mUsers[position]

        val nameTextView = viewHolder.nameTextView
        nameTextView.text = user.name
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }
}