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
import com.assign3.addressbook.activities.LocationDetailsActivity
import com.assign3.addressbook.activities.UserDetailsActivity
import com.assign3.addressbook.models.Location

class LocationsListAdapter(var mLocations: List<Location> = ArrayList(), var userName: String = ""): RecyclerView.Adapter<LocationsListAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.locationName)
        val addressTextView: TextView = itemView.findViewById(R.id.locationAddress)
        val mapButton: Button = itemView.findViewById(R.id.viewMapButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsListAdapter.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)

        val locationView = inflater.inflate(R.layout.item_locations_list, parent, false)

        return ViewHolder(locationView)
    }

    override fun onBindViewHolder(viewHolder: LocationsListAdapter.ViewHolder, position: Int) {
        val location: Location = mLocations[position]

        val nameTextView = viewHolder.nameTextView
        nameTextView.text = location.name

        val addressTextView = viewHolder.addressTextView
        addressTextView.text = location.address

        val mapButton = viewHolder.mapButton

        mapButton.setOnClickListener {
            val intent = Intent(context, LocationDetailsActivity::class.java)
            intent.putExtra("Username", userName)
            intent.putExtra("lat", location.latitude)
            intent.putExtra("long", location.longitude)
            intent.putExtra("address", location.address)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mLocations.size
    }
}