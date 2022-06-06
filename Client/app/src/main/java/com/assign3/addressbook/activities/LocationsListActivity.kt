package com.assign3.addressbook.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assign3.addressbook.R
import com.assign3.addressbook.adapters.LocationsAdapter
import com.assign3.addressbook.api.ApiInterface

class LocationsListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locations_list)

        val addContactButton: View = findViewById(R.id.addContactButton);
        val intent = Intent(this@LocationsListActivity, AddLocationActivity::class.java);

        addContactButton.setOnClickListener{
            startActivity(intent);
        }
    }
}