package com.assign3.addressbook.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.assign3.addressbook.R

class AddLocationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_location)

        val bundle: Bundle? = intent.extras;
        val name: String? = intent.getStringExtra("Username");

        val intent = Intent(this@AddLocationActivity, LocationsListActivity::class.java);

        val button: Button = findViewById(R.id.addContactButton);

        button.setOnClickListener{
            intent.putExtra("Username", name);
            startActivity(intent);
        }
    }
}