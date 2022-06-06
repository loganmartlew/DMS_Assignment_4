package com.assign3.addressbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    val username = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var buttonLogin: Button = findViewById(R.id.LoginButton);
        val intent = Intent(this@MainActivity, ContactsPage::class.java);
        var username: EditText = findViewById(R.id.Nameet);
        var uName = username.text.toString()

        buttonLogin.setOnClickListener{
            intent.putExtra("Username", uName);
            startActivity(intent);
        }
    }
}