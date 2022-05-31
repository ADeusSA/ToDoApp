package ru.iabarmin.todoapp.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import ru.iabarmin.todoapp.CentralActivity
import ru.iabarmin.todoapp.MainActivity
import ru.iabarmin.todoapp.R
import ru.iabarmin.todoapp.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater)

        binding.apply {
            btnBackSettings.setOnClickListener {
                val i = Intent(activity, CentralActivity::class.java)
                startActivity(i)
                activity?.finish()
            }
            btnAccSet.setOnClickListener {
                findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToAccountSettingsFragment())
            }
            btnGeneralSet.setOnClickListener {
                findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToBaseSettingsFragment())
            }
            btnExitSet.setOnClickListener {
                exit()
            }
        }

        return binding.root
    }

    private fun exit() {
        AlertDialog.Builder(requireContext())
            .setTitle("Выход из учетной записи")
            .setMessage("Вы уверены, что хотите выйти?")
            .setPositiveButton("Да"){dialog, _ ->

                deleteLocalUserData()
                val i = Intent(activity, MainActivity::class.java)
                startActivity(i)
                activity?.finish()

                dialog.dismiss()
            }.setNegativeButton("Нет"){dialog, _ ->
                dialog.dismiss()
            }.create().show()
    }

    private fun deleteLocalUserData() {
        val sharedPrefs = activity?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        //val savedName = sharedPrefs.getString("NAME_KEY", null)
        //val savedEmail = sharedPrefs.getString("EMAIL_KEY", null)

        val editor = sharedPrefs?.edit()
        editor?.apply {
            putString("NAME_KEY",null)
            putString("EMAIL_KEY", null)
        }?.apply()
        Toast.makeText(activity,
            "Вы вышли из аккаунта!", Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }
}