package ru.iabarmin.todoapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.iabarmin.todoapp.signup.LoginActivity
import ru.iabarmin.todoapp.signup.RegistrationActivity

class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Удаление Title Bar
        try { this.supportActionBar!!.hide() } catch (e: NullPointerException) { }



        button_goToLog.setOnClickListener {
            val logIntent = Intent(this, LoginActivity::class.java)
            startActivity(logIntent)
            finish()
        }
        button_goToReg.setOnClickListener {
            val regIntent = Intent(this, RegistrationActivity::class.java)
            startActivity(regIntent)
            finish()
        }
    }
}