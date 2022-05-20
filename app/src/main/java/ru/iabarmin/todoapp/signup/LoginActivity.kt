package ru.iabarmin.todoapp.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.iabarmin.todoapp.data.User
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.iabarmin.todoapp.CentralActivity
import ru.iabarmin.todoapp.R
import ru.iabarmin.todoapp.remote.RetrofitInterface


class LoginActivity : AppCompatActivity() {

    private var email: String? = null
    private var password: String? = null
    private lateinit var retrofit: Retrofit
    private lateinit var retrofitInterface: RetrofitInterface
    private val BASE_URL = "http://10.0.2.2:3000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofitInterface = retrofit.create(RetrofitInterface::class.java)

        buttonLog.setOnClickListener {
            email = editTextEmailLog.text.toString().trim()
            password = editTextPasswordLog.text.toString().trim()
            if (validateInputs()) {
                handleLogin()
            }
        }

        buttonForget.setOnClickListener {

        }
    }
    /**
     * Функция выполняет вызов к серверу для авторизации пользователя
     */
    private fun handleLogin() {
        val map: HashMap<String, String> = HashMap()
        map["email"] = email!!
        map["password"] = password!!

        val call: Call<User> = retrofitInterface.executeLogin(map)

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.code() == 200) {
                    //val result: User? = response.body()
                    val i = Intent(this@LoginActivity, CentralActivity::class.java)
                    startActivity(i)
                    finish()
                    Log.d("MYTAG", "check")
                } else if (response.code() == 404) {
                    Toast.makeText(this@LoginActivity, "Пожалуйста проверьте введенные данные",
                        Toast.LENGTH_LONG).show();
                }

            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                RegistrationActivity().toastInputError("Ошибка соединения")
            }

        })
    }

    /**
     * Проверка валидности введенных пользователем данных при авторизации
     */
    private fun validateInputs(): Boolean {
        if (RegistrationActivity().checkNullorBlank(email)) {
            editTextEmailLog.error = "Поле не может быть пустым"
            editTextEmailLog.requestFocus()
            return false
        }
        if (RegistrationActivity().checkNullorBlank(password)) {
            editTextPasswordLog.error = "Поле не может быть пустым"
            editTextPasswordLog.requestFocus()
            return false
        }
        return true
    }

    /**
     * Добавление функции "Шаг назад в ToolBar
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id = item.itemId
        if (id == android.R.id.home) {
            this.finish()
        }
        return super.onOptionsItemSelected(item)
    }

}