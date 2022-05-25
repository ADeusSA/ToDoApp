package ru.iabarmin.todoapp.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import ru.iabarmin.todoapp.CentralActivity
import ru.iabarmin.todoapp.MainActivity
import ru.iabarmin.todoapp.R
import ru.iabarmin.todoapp.databinding.FragmentIntroBinding

class IntroFragment : Fragment() {

    private lateinit var binding: FragmentIntroBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentIntroBinding.inflate(inflater)

        binding.btnGoToLog.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_loginFragment)
        }
        binding.btnGoToReg.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_registrationFragment)
        }

        binding.btnTest.setOnClickListener {
            saveUserData()

            val i = Intent(activity, CentralActivity::class.java)
            startActivity(i)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    private fun saveUserData() {
        val saveName = "name"
        val saveEmail = "email"

        val sharedPrefs = activity?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs?.edit()
        editor?.apply {
            putString("NAME_KEY",saveName)
            putString("EMAIL_KEY", saveEmail)
        }?.apply()
        val toast = Toast.makeText(activity, "Данные сохранены", Toast.LENGTH_LONG)
        toast.show()
    }
}