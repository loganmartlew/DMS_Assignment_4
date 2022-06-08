package com.assign3.addressbook.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.assign3.addressbook.R
import com.assign3.addressbook.adapters.LocationsListAdapter
import com.assign3.addressbook.adapters.NotificationsListAdapter
import com.assign3.addressbook.api.ApiInterface
import com.assign3.addressbook.models.Request
import com.assign3.addressbook.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsListFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = requireActivity();
        activity.title = getString(R.string.activity_title_notifications_list)
        return inflater.inflate(R.layout.fragment_notifications_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity();

        val name: String = activity.intent.getStringExtra("Username") as String

        val locationsView: RecyclerView = activity.findViewById(R.id.rvNotifications)
        val adapter = NotificationsListAdapter()
        locationsView.adapter = adapter
        locationsView.layoutManager = LinearLayoutManager(activity)

        val refreshLayout: SwipeRefreshLayout = activity.findViewById(R.id.refreshNotifications)
        refreshLayout.setOnRefreshListener {
            updateAdapter(adapter, name) {
                refreshLayout.isRefreshing = false
            }
        }

        updateAdapter(adapter, name) {
            refreshLayout.isRefreshing = false
        }
    }

    private fun updateAdapter(adapter: NotificationsListAdapter, name: String, callback: () -> Unit) {
        val apiInterface = ApiInterface.create().getIncomingRequests(name)
        apiInterface.enqueue(object: Callback<List<Request>> {
            override fun onResponse(call: Call<List<Request>>, response: Response<List<Request>>) {
                if(response?.body() != null) {
                    adapter.mRequests = response.body()!!
                    adapter.notifyDataSetChanged()
                    callback()
                }
            }

            override fun onFailure(call: Call<List<Request>>?, t: Throwable?) {
                callback()
            }
        })
    }
}