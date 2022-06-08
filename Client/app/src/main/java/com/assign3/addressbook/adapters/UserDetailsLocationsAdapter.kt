package com.assign3.addressbook.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.assign3.addressbook.R
import com.assign3.addressbook.models.Location
import com.assign3.addressbook.models.Request
import com.assign3.addressbook.models.RequestStatus

class UserDetailsLocationsAdapter(var mLocations: List<Location> = ArrayList(), var mRequests: List<Request> = ArrayList()): RecyclerView.Adapter<UserDetailsLocationsAdapter.ViewHolder>()  {

    private lateinit var context: Context;

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.userDetailsLocationName)
        var detailsButton: Button = itemView.findViewById(R.id.detailsButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDetailsLocationsAdapter.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)

        val locationView = inflater.inflate(R.layout.item_user_locations_list, parent, false)
        return ViewHolder(locationView)
    }

    override fun onBindViewHolder(viewHolder: UserDetailsLocationsAdapter.ViewHolder, position: Int) {
        val location: Location = mLocations[position]

        viewHolder.detailsButton.setOnClickListener {
            Log.d("Details", "Request location")
        }

        mRequests.forEach {
            if (it.location.id == location.id) {
                Log.d("Status", it.status)
                when (it.status) {
                    RequestStatus.PENDING.text -> {
                        viewHolder.detailsButton.text = context.getString(R.string.action_request_pending)
                        viewHolder.detailsButton.isEnabled = false
                    }
                    RequestStatus.ACCEPTED.text -> {
                        viewHolder.detailsButton.text = context.getString(R.string.action_request_accepted)
                        viewHolder.detailsButton.setOnClickListener {
                            Log.d("Details", "View details")
                        }
                    }
                    RequestStatus.REJECTED.text -> {
                        viewHolder.detailsButton.text = context.getString(R.string.action_request_rejected)
                        viewHolder.detailsButton.isEnabled = false
                    }
                }

            }
        }

        val nameTextView = viewHolder.nameTextView
        nameTextView.text = location.name
    }

    override fun getItemCount(): Int {
        return mLocations.size
    }
}