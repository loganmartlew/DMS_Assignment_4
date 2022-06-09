package com.assign3.addressbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.assign3.addressbook.activities.AppActivity
import com.assign3.addressbook.api.ApiInterface
import com.assign3.addressbook.api.UserDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(view: View) {
        val intent = Intent(this@MainActivity, AppActivity::class.java);
        val nameInput = findViewById<EditText>(R.id.nameInput)
        val name = nameInput.text.toString()

        val apiInterface = ApiInterface.create().login(UserDTO(name))
        apiInterface.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    intent.putExtra("Username", name);
                    startActivity(intent)
                } else {
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
            }
        })
    }
}