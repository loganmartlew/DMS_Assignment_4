package com.assign3.addressbook.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.assign3.addressbook.R

class LocationsListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_page)

        val addContactButton: View = findViewById(R.id.addContactButton);
        val intent = Intent(this@LocationsListActivity, AddLocationActivity::class.java);

        addContactButton.setOnClickListener{
            startActivity(intent);
        }
    }
}