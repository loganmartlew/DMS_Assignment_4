package com.assign3.addressbook.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.assign3.addressbook.R
import com.assign3.addressbook.models.Request

class NotificationsListAdapter(var mRequests: List<Request> = ArrayList()): RecyclerView.Adapter<NotificationsListAdapter.ViewHolder>() {

    lateinit var update: () -> Unit

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val requestMessage: TextView = itemView.findViewById(R.id.requestMessage)
        val requestLocationName: TextView = itemView.findViewById(R.id.requestLocationName)
        val acceptButton: Button = itemView.findViewById(R.id.acceptButton)
        val denyButton: Button = itemView.findViewById(R.id.denyButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsListAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val locationView = inflater.inflate(R.layout.item_notifications_list, parent, false)
        return ViewHolder(locationView)
    }

    override fun onBindViewHolder(viewHolder: NotificationsListAdapter.ViewHolder, position: Int) {
        val request: Request = mRequests[position]

        val fromUserName = request.fromUser.name

        val requestMessage = viewHolder.requestMessage
        requestMessage.text = "$fromUserName wants access to a location:"

        val requestLocationName = viewHolder.requestLocationName
        requestLocationName.text = request.location.name


    }

    override fun getItemCount(): Int {
        return mRequests.size
    }
}