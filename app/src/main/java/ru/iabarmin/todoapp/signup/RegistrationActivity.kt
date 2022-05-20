package ru.iabarmin.todoapp.signup

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.iabarmin.todoapp.CentralActivity
import ru.iabarmin.todoapp.R
import ru.iabarmin.todoapp.remote.RetrofitInterface

class RegistrationActivity : AppCompatActivity() {

    private var username: String? = null
    private var password: String? = null
    private var confirmPassword: String? = null
    private var email: String? = null

    private lateinit var retrofit: Retrofit
    private lateinit var retrofitInterface: RetrofitInterface
    private val BASE_URL = "http://10.0.2.2:3000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofitInterface = retrofit.create(RetrofitInterface::class.java)

        buttonCreateUser.setOnClickListener {
            username = editTextNameReg.text.toString().trim()
            email = editTextEmailReg.text.toString().trim()
            password = editTextPasswordReg.text.toString().trim()
            confirmPassword = editTextPasswordReg2.text.toString().trim()
            if (validateInputs()) {
                handleSignup()
                val i = Intent(this, CentralActivity::class.java)
                startActivity(i)
                finish()
            }
        }

    }

    /**
     * Функция выполняет вызов к серверу для регистрации пользователя
     */
    private fun handleSignup() {
        val map: HashMap<String, String> = HashMap()

        map["name"] = username!!
        map["email"] = email!!
        map["password"] = password!!

        val call: Call<Void> = retrofitInterface.executeSignup(map)

        call.enqueue(object: Callback<Void> {

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == 200) {
                    toastInputError("Регистрация выполнена успешно!")
                } else if (response.code() == 400) {
                    toastInputError("Имя пользователя или адрес электронной почты уже заняты")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                toastInputError("Ошибка соединения!")
            }
        })
    }

    /**
     * Проверка валидности введенных пользователем данных при регистрации
     */
    private fun validateInputs(): Boolean {
        if (checkNullorBlank(email)) {
            editTextEmailReg.error = "Поле не может быть пустым"
            editTextEmailReg.requestFocus()
            return false
        }
        if (checkNullorBlank(username)) {
            editTextNameReg.error = "Поле не может быть пустым"
            editTextNameReg.requestFocus()
            return false
        }
        if (checkNullorBlank(password)) {
            editTextPasswordReg.error = "Поле не может быть пустым"
            editTextPasswordReg.requestFocus()
            return false
        }
        if (checkNullorBlank(confirmPassword)) {
            editTextPasswordReg2.error = "Поле не может быть пустым"
            editTextPasswordReg2.requestFocus()
            return false
        }
        if (password != confirmPassword) {
            editTextPasswordReg2.error = "Поле не может быть пустым"
            editTextPasswordReg2.requestFocus()
            return false
        }
        return true
    }

    fun checkNullorBlank(str: String?) : Boolean {
        if (str.isNullOrBlank()) return true
        if (str.contains(" ", ignoreCase = true)) return true
        return false
    }

    fun toastInputError(str: String) {
        val toast = Toast.makeText(applicationContext, str, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
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