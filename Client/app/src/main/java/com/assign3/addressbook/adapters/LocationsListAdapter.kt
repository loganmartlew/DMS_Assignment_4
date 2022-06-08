package com.assign3.addressbook.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.assign3.addressbook.R
import com.assign3.addressbook.models.Location

class LocationsListAdapter(var mLocations: List<Location> = ArrayList()) : RecyclerView.Adapter<LocationsListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.locationName)
        val addressTextView: TextView = itemView.findViewById(R.id.locationAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsListAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val locationView = inflater.inflate(R.layout.location_list_item, parent, false)
        return ViewHolder(locationView)
    }

    override fun onBindViewHolder(viewHolder: LocationsListAdapter.ViewHolder, position: Int) {
        val location: Location = mLocations[position]

        val nameTextView = viewHolder.nameTextView
        nameTextView.text = location.name

        val addressTextView = viewHolder.addressTextView
        addressTextView.text = location.address
    }

    override fun getItemCount(): Int {
        return mLocations.size
    }
}