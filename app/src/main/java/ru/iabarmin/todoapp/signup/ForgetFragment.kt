package ru.iabarmin.todoapp.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.iabarmin.todoapp.CentralActivity
import ru.iabarmin.todoapp.R
import ru.iabarmin.todoapp.databinding.FragmentForgetBinding
import ru.iabarmin.todoapp.remote.RetrofitInterface

class ForgetFragment : Fragment() {

    private var email: String? = null
    private var name: String? = null
    private var password: String? = null
    private lateinit var retrofit: Retrofit
    private lateinit var retrofitInterface: RetrofitInterface
    private val BASE_URL = "http://10.0.2.2:3000"
    private lateinit var binding: FragmentForgetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgetBinding.inflate(inflater)

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofitInterface = retrofit.create(RetrofitInterface::class.java)

        binding.btnForget.setOnClickListener {
            email = binding.editEmailForget.text.toString().trim()
            name = binding.editNameForget.text.toString().trim()
            password = binding.editPassForget.text.toString().trim()
            if (validateInputs()) {
                handleForget()
            }
        }


        binding.btnBackForget.setOnClickListener {
            findNavController().navigate(R.id.action_forgetFragment_to_loginFragment)
        }

        return binding.root
    }

    private fun handleForget() {
        val map: HashMap<String, String> = HashMap()
        map["username"] = name!!
        map["email"] = email!!
        map["password"] = password!!

        val call: Call<Void> = retrofitInterface.executeForget(map)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == 200) {
                    toastInputError("Пароль успешно изменен!")

                    saveUserData(email!!, name!!)

                    val i = Intent(activity, CentralActivity::class.java)
                    startActivity(i)
                    activity?.finish()
                } else if (response.code() == 404) {
                    toastInputError("Пожалуйста проверьте введенные данные")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                toastInputError("Ошибка соединения")
            }

        })
    }

    private fun validateInputs(): Boolean {
        val str = "Поле не может быть пустым"

        if (RegistrationFragment().checkNullorBlank(email)) {
            binding.editEmailForget.error = str
            binding.editEmailForget.requestFocus()
            return false
        }
        if (RegistrationFragment().checkNullorBlank(name)) {
            binding.editNameForget.error = str
            binding.editNameForget.requestFocus()
            return false
        }
        if (RegistrationFragment().checkNullorBlank(password)) {
            binding.editPassForget.error = str
            binding.editPassForget.requestFocus()
            return false
        }
        return true
    }

    private fun toastInputError(str: String) {
        val toast = Toast.makeText(activity, str, Toast.LENGTH_LONG)
        toast.show()
    }

    private fun saveUserData(saveEmail: String,saveUsername: String) {
        val sharedPrefs = activity?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs?.edit()
        editor?.apply {
            putString("NAME_KEY", saveUsername)
            putString("EMAIL_KEY", saveEmail)
        }?.apply()
        toastInputError("UserData Saved")
    }
}