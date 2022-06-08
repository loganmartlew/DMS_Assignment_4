package com.assign3.addressbook.activities

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assign3.addressbook.R
import com.assign3.addressbook.adapters.LocationsListAdapter
import com.assign3.addressbook.adapters.UserDetailsLocationsAdapter
import com.assign3.addressbook.api.ApiInterface
import com.assign3.addressbook.models.Request
import com.assign3.addressbook.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        val name: String = intent.getStringExtra("Username") as String
        val userDetailsName: String = intent.getStringExtra("UsernameDetails") as String

        val locationsView: RecyclerView = findViewById(R.id.rvUserLocations)
        val adapter = UserDetailsLocationsAdapter()
        locationsView.adapter = adapter
        locationsView.layoutManager = LinearLayoutManager(this)

        val userNameText: TextView = findViewById(R.id.userDetailsName)
        userNameText.text = userDetailsName

        val apiGetUser = ApiInterface.create().getUser(userDetailsName)
        apiGetUser.enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response?.body() != null) {
                    val user = response.body()!!

                    userNameText.text = user.name

                    adapter.mLocations = user.locations
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<User>?, t: Throwable?) {
            }
        })

        val apiGetRequests = ApiInterface.create().getOutgoingRequests(name)
        apiGetRequests.enqueue(object: Callback<List<Request>> {
            override fun onResponse(call: Call<List<Request>>, response: Response<List<Request>>) {
                if(response?.body() != null) {
                    val requests = response.body()!!

                    adapter.mRequests = requests
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Request>>?, t: Throwable?) {
            }
        })
    }
}