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
import com.assign3.addressbook.activities.AddLocationActivity
import com.assign3.addressbook.adapters.LocationsListAdapter
import com.assign3.addressbook.api.ApiInterface
import com.assign3.addressbook.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationsListFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = requireActivity();
        activity.title = getString(R.string.activity_title_locations_list)
        return inflater.inflate(R.layout.fragment_locations_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity();

        val name: String = activity.intent.getStringExtra("Username") as String

        val locationsView: RecyclerView = activity.findViewById(R.id.rvLocations)
        val adapter = LocationsListAdapter()
        adapter.userName = name;
        locationsView.adapter = adapter
        locationsView.layoutManager = LinearLayoutManager(activity)

        val refreshLayout: SwipeRefreshLayout = activity.findViewById(R.id.refreshLocations)
        refreshLayout.setOnRefreshListener {
            updateAdapter(adapter, name) {
                refreshLayout.isRefreshing = false
            }
        }

        updateAdapter(adapter, name) {
            refreshLayout.isRefreshing = false
        }

        val addContactButton: View = activity.findViewById(R.id.addContactButton);
        addContactButton.setOnClickListener{
            val intent = Intent(activity, AddLocationActivity::class.java);
            intent.putExtra("Username", name);
            startActivity(intent);
        }
    }

    private fun updateAdapter(adapter: LocationsListAdapter, name: String, callback: () -> Unit) {
        val apiInterface = ApiInterface.create().getUser(name)
        apiInterface.enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response?.body() != null) {
                    adapter.mLocations = response.body()!!.locations
                    adapter.notifyDataSetChanged()
                    callback()
                }
            }

            override fun onFailure(call: Call<User>?, t: Throwable?) {
                callback()
            }
        })
    }
}