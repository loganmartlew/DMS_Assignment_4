package com.assign3.addressbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import com.assign3.addressbook.activities.AppActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(view: View) {
        val intent = Intent(this@MainActivity, AppActivity::class.java);
        val nameInput = findViewById<EditText>(R.id.nameInput)
        val name = nameInput.text.toString()

        Log.i("login", name)
        intent.putExtra("Username", name);
        startActivity(intent);
    }
}