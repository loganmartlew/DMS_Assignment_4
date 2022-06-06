package com.assign3.addressbook.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.assign3.addressbook.R
import com.assign3.addressbook.adapters.LocationsAdapter
import com.assign3.addressbook.api.ApiInterface
import com.assign3.addressbook.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationsListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locations_list)

        val bundle: Bundle? = intent.extras
        val name: String = intent.getStringExtra("Username") as String

        val locationsView: RecyclerView = findViewById(R.id.rvLocations)
        val adapter = LocationsAdapter()
        locationsView.adapter = adapter
        locationsView.layoutManager = LinearLayoutManager(this)

        val refreshLayout: SwipeRefreshLayout = findViewById(R.id.refreshLocations)
        refreshLayout.setOnRefreshListener {
            updateAdapter(adapter, name) {
                refreshLayout.isRefreshing = false
            }
        }

        updateAdapter(adapter, name) {
            refreshLayout.isRefreshing = false
        }

        val addContactButton: View = findViewById(R.id.addContactButton);
        val intent = Intent(this@LocationsListActivity, AddLocationActivity::class.java);

        addContactButton.setOnClickListener{
            intent.putExtra("Username", name);
            startActivity(intent);
        }
    }

    fun updateAdapter(adapter: LocationsAdapter, name: String, callback: () -> Unit) {
        val apiInterface = ApiInterface.create().getUser(name)
        apiInterface.enqueue(object: Callback<User>{
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