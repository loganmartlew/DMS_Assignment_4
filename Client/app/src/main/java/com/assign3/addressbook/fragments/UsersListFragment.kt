package com.assign3.addressbook.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.assign3.addressbook.R
import com.assign3.addressbook.adapters.UsersAdapter
import com.assign3.addressbook.api.ApiInterface
import com.assign3.addressbook.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersListFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = requireActivity();
        activity.title = getString(R.string.activity_title_users_list)
        return inflater.inflate(R.layout.fragment_users_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity();

        val locationsView: RecyclerView = activity.findViewById(R.id.rvUsers)
        val adapter = UsersAdapter()
        locationsView.adapter = adapter
        locationsView.layoutManager = LinearLayoutManager(activity)

        val refreshLayout: SwipeRefreshLayout = activity.findViewById(R.id.refreshUsers)
        refreshLayout.setOnRefreshListener {
            updateAdapter(adapter) {
                refreshLayout.isRefreshing = false
            }
        }

        updateAdapter(adapter) {
            refreshLayout.isRefreshing = false
        }
    }

    private fun updateAdapter(adapter: UsersAdapter, callback: () -> Unit) {
        val apiInterface = ApiInterface.create().getUsers()
        apiInterface.enqueue(object: Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if(response?.body() != null) {
                    adapter.mUsers = response.body()!!
                    adapter.notifyDataSetChanged()
                    callback()
                }
            }

            override fun onFailure(call: Call<List<User>>?, t: Throwable?) {
                callback()
            }
        })
    }
}