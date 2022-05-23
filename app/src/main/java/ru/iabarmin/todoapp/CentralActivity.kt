package ru.iabarmin.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import ru.iabarmin.todoapp.R


class CentralActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_central)
        //setupActionBarWithNavController(findNavController(R.id.nav_host_fragment))
    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment)
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }
}