package ru.iabarmin.todoapp.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.iabarmin.todoapp.CentralActivity
import ru.iabarmin.todoapp.R
import ru.iabarmin.todoapp.data.User
import ru.iabarmin.todoapp.databinding.FragmentLoginBinding
import ru.iabarmin.todoapp.remote.RetrofitInterface


class LoginFragment : Fragment() {

    private var emailLog: String? = null
    private var passwordLog: String? = null
    private lateinit var retrofit: Retrofit
    private lateinit var retrofitInterface: RetrofitInterface
    private val BASE_URL = "http://10.0.2.2:3000"

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)

        val gson = GsonBuilder()
            .setLenient()
            .create()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        retrofitInterface = retrofit.create(RetrofitInterface::class.java)

        binding.btnLog.setOnClickListener {
            emailLog = binding.editEmailLog.text.toString().trim()
            passwordLog = binding.editPasswordLog.text.toString().trim()
            if (validateInputs()) {
                handleLogin()
            }
        }

        binding.btnFgt.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetFragment)
        }

        binding.btnBackLog.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_introFragment)
        }

        return binding.root
    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        (requireActivity() as AppCompatActivity).supportActionBar?.show()
//    }

    /**
     * Функция выполняет запрос к серверу для авторизации пользователя
     */
    private fun handleLogin() {
        val map: HashMap<String, String> = HashMap()
        map["email"] = emailLog!!
        map["password"] = passwordLog!!

        val call: Call<User> = retrofitInterface.executeLogin(map)

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.code() == 200) {

                    val result = response.body()!!
                    val usernameSave = result.username
                    val idSave = result.id
                    saveUserData(emailLog!!, usernameSave, idSave)

                    val i = Intent(activity, CentralActivity::class.java)
                    startActivity(i)
                    activity?.finish()
                } else if (response.code() == 404) {
                    toastInputError("Пожалуйста проверьте введенные данные")
                }

            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                toastInputError("Ошибка соединения")
            }

        })
    }

    /**
     * Проверка валидности введенных пользователем данных при авторизации
     */
    private fun validateInputs(): Boolean {
        val str = "Поле не может быть пустым"

        if (RegistrationFragment().checkNullorBlank(emailLog)) {
            binding.editEmailLog.error = str
            binding.editEmailLog.requestFocus()
            return false
        }
        if (RegistrationFragment().checkNullorBlank(passwordLog)) {
            binding.editPasswordLog.error = str
            binding.editPasswordLog.requestFocus()
            return false
        }
        return true
    }

    private fun toastInputError(str: String) {
        val toast = Toast.makeText(activity, str, Toast.LENGTH_LONG)
        toast.show()
    }

    private fun saveUserData(saveEmail: String,saveUsername: String, idSave: String) {
        val sharedPrefs = activity?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs?.edit()
        editor?.apply {
            putString("NAME_KEY", saveUsername)
            putString("EMAIL_KEY", saveEmail)
            putString("ID_KEY", idSave)
        }?.apply()
        toastInputError("UserData Saved")
    }

}