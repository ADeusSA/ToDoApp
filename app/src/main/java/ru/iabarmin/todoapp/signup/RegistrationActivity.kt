package ru.iabarmin.todoapp.signup

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.iabarmin.todoapp.CentralActivity
import ru.iabarmin.todoapp.R
import ru.iabarmin.todoapp.databinding.ActivityRegistrationBinding
import ru.iabarmin.todoapp.remote.RetrofitInterface

class RegistrationActivity : AppCompatActivity() {

    private var username: String? = null
    private var password: String? = null
    private var confirmPassword: String? = null
    private var email: String? = null

    private lateinit var retrofit: Retrofit
    private lateinit var retrofitInterface: RetrofitInterface
    private val BASE_URL = "http://10.0.2.2:3000"

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofitInterface = retrofit.create(RetrofitInterface::class.java)

        binding.btnCreateUser.setOnClickListener {
            username = binding.editNameReg.text.toString().trim()
            email = binding.editEmailReg.text.toString().trim()
            password = binding.editPasswordReg.text.toString().trim()
            confirmPassword = binding.editPasswordReg2.text.toString().trim()
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
        var str = "Поле не может быть пустым"

        if (checkNullorBlank(email)) {
            binding.editEmailReg.error = str
            binding.editEmailReg.requestFocus()
            return false
        }
        if (checkNullorBlank(username)) {
            binding.editNameReg.error = str
            binding.editNameReg.requestFocus()
            return false
        }
        if (checkNullorBlank(password)) {
            binding.editPasswordReg.error = str
            binding.editPasswordReg.requestFocus()
            return false
        }
        if (checkNullorBlank(confirmPassword)) {
            binding.editPasswordReg2.error = str
            binding.editPasswordReg2.requestFocus()
            return false
        }
        if (password != confirmPassword) {
            binding.editPasswordReg2.error = str
            binding.editPasswordReg2.requestFocus()
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