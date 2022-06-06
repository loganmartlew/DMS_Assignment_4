package com.assign3.addressbook.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.assign3.addressbook.R
import com.assign3.addressbook.models.Location

class LocationsAdapter(private val mLocations: List<Location>) : RecyclerView.Adapter<LocationsAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.locationAddress)
        val addressTextView: TextView = itemView.findViewById(R.id.locationName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val locationView = inflater.inflate(R.layout.location_list_item, parent, false)
        return ViewHolder(locationView)
    }

    override fun onBindViewHolder(viewHolder: LocationsAdapter.ViewHolder, position: Int) {
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