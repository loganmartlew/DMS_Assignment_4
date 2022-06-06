package com.assign3.addressbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ContactsPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_page)

        val addContactButton: View = findViewById(R.id.addContactButton);
        val intent = Intent(this@ContactsPage, addContact::class.java);

        addContactButton.setOnClickListener{
            startActivity(intent);
        }
    }
}