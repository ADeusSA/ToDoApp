package ru.iabarmin.todoapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView
import ru.iabarmin.todoapp.R


class CentralActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_central)

        var drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.navView)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open,
        R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.settingsFragment -> deleteUserData()
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteUserData() {
        val sharedPrefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val savedName = sharedPrefs.getString("NAME_KEY", null)
        val savedEmail = sharedPrefs.getString("EMAIL_KEY", null)

        val editor = sharedPrefs?.edit()
        editor?.apply {
            putString("NAME_KEY",null)
            putString("EMAIL_KEY", null)
        }?.apply()
        Toast.makeText(applicationContext,
            "Data deleted", Toast.LENGTH_SHORT).show()
    }

}