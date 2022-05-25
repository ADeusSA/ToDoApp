package ru.iabarmin.todoapp.signup

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.iabarmin.todoapp.CentralActivity
import ru.iabarmin.todoapp.MainActivity
import ru.iabarmin.todoapp.R
import ru.iabarmin.todoapp.databinding.FragmentRegistrationBinding
import ru.iabarmin.todoapp.remote.RetrofitInterface

class RegistrationFragment : Fragment() {

    private var username: String? = null
    private var password: String? = null
    private var confirmPassword: String? = null
    private var email: String? = null

    private lateinit var retrofit: Retrofit
    private lateinit var retrofitInterface: RetrofitInterface
    private val BASE_URL = "http://10.0.2.2:3000"

    private lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = FragmentRegistrationBinding.inflate(inflater)
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
            }
        }

        binding.btnBackReg.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_introFragment)
        }

        return binding.root
    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        (requireActivity() as AppCompatActivity).supportActionBar?.show()
//    }

    /**
     * Функция выполняет вызов к серверу для регистрации пользователя
     */
    private fun handleSignup() {
        val map: HashMap<String, String> = HashMap()

        map["username"] = username!!
        map["email"] = email!!
        map["password"] = password!!

        val call: Call<Void> = retrofitInterface.executeSignup(map)

        call.enqueue(object: Callback<Void> {

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == 200) {

                    saveUserData()

                    val i = Intent(activity, CentralActivity::class.java)
                    startActivity(i)
                    activity?.finish()
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
        val str = "Поле не может быть пустым"

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
            binding.editPasswordReg2.error = "Пароли должны совпадать"
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

    private fun toastInputError(str: String) {
        val toast = Toast.makeText(activity, str, Toast.LENGTH_LONG)
        toast.show()
    }

    private fun saveUserData() {
        val saveName = username
        val saveEmail = email

        val sharedPrefs = activity?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs?.edit()
        editor?.apply {
            putString("NAME_KEY",saveName)
            putString("EMAIL_KEY", saveEmail)
        }?.apply()
        toastInputError("UserData Saved")
    }

}