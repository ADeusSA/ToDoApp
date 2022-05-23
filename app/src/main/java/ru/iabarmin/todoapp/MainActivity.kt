package ru.iabarmin.todoapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import ru.iabarmin.todoapp.databinding.ActivityMainBinding
import ru.iabarmin.todoapp.signup.LoginActivity
import ru.iabarmin.todoapp.signup.RegistrationActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        /**
         * Удаление Title Bar
         */
        try { this.supportActionBar!!.hide() } catch (e: NullPointerException) { }

        binding.btnGoToLog.setOnClickListener {
            val logIntent = Intent(this, LoginActivity::class.java)
            startActivity(logIntent)
        }
        binding.btnGoToReg.setOnClickListener {
            val regIntent = Intent(this, RegistrationActivity::class.java)
            startActivity(regIntent)
        }
    }
}