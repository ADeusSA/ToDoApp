package ru.iabarmin.todoapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        loadUserData()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun loadUserData() {
        val sharedPrefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val savedName = sharedPrefs.getString("NAME_KEY", null)
        val savedEmail = sharedPrefs.getString("EMAIL_KEY", null)
        if(savedEmail != null && savedName != null) {
            val i = Intent(this, CentralActivity::class.java)
            startActivity(i)
            finish()
        }
    }

}