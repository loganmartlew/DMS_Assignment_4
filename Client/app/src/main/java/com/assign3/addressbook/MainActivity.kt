package com.assign3.addressbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import com.assign3.addressbook.activities.LocationsListActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonLogin: Button = findViewById(R.id.LoginButton);
        val intent = Intent(this@MainActivity, LocationsListActivity::class.java);
        var username: EditText = findViewById(R.id.Nameet);
        var uName = username.text.toString()

        buttonLogin.setOnClickListener{
            intent.putExtra("Username", uName);
            startActivity(intent);
        }
    }
}