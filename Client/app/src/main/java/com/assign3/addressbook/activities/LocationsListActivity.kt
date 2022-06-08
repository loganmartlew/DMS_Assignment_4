package com.assign3.addressbook.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
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
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locations_list)

        val drawerLayout: DrawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }
}