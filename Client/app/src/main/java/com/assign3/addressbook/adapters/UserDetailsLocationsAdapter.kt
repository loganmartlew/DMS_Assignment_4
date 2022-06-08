package com.assign3.addressbook.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.assign3.addressbook.R
import com.assign3.addressbook.models.Location

class UserDetailsLocationsAdapter(var mLocations: List<Location> = ArrayList()): RecyclerView.Adapter<UserDetailsLocationsAdapter.ViewHolder>()  {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.userDetailsLocationName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDetailsLocationsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val locationView = inflater.inflate(R.layout.item_user_locations_list, parent, false)
        return ViewHolder(locationView)
    }

    override fun onBindViewHolder(viewHolder: UserDetailsLocationsAdapter.ViewHolder, position: Int) {
        val location: Location = mLocations[position]

        val nameTextView = viewHolder.nameTextView
        nameTextView.text = location.name
    }

    override fun getItemCount(): Int {
        return mLocations.size
    }
}