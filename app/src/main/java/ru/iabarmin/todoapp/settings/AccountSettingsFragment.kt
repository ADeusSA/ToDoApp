package ru.iabarmin.todoapp.settings

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import ru.iabarmin.todoapp.R
import ru.iabarmin.todoapp.databinding.FragmentAccountSettingsBinding


class AccountSettingsFragment : Fragment() {

    private lateinit var binding: FragmentAccountSettingsBinding
    private var savedName: String? = null
    private var savedEmail: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAccountSettingsBinding.inflate(inflater)

        loadLocalData()

        binding.apply {
            tvNameSettings.text = "Имя\n" + savedName
            tvEmailSettings.text = "Email\n" + savedEmail

            btnBackAccountSettings.setOnClickListener {
                findNavController().navigate(AccountSettingsFragmentDirections.actionAccountSettingsFragmentToSettingsFragment())
            }

            tvNameSettings.setOnClickListener {
                editName()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    private fun loadLocalData() {
        val sharedPrefs = activity?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        savedName = sharedPrefs?.getString("NAME_KEY", null)
        savedEmail = sharedPrefs?.getString("EMAIL_KEY", null)
    }

    private fun editName() {
        val mBuilder = AlertDialog.Builder(requireContext())
        val mLayout = LinearLayout(requireContext())
        val tvName = TextView(requireContext())
        val etName = EditText(requireContext())
        tvName.text = "Введите новое имя пользователя:"
        tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        etName.setSingleLine()
        etName.hint = "Имя"
        mLayout.orientation = LinearLayout.VERTICAL
        mLayout.addView(tvName)
        mLayout.addView(etName)
        mLayout.setPadding(50, 40, 50, 10)
        mBuilder.setView(mLayout)

        mBuilder.setPositiveButton("Сохранить") { _, _ ->
            var newName = etName.text.toString()
        }
        mBuilder.setNeutralButton("Отмена") {dialog, _ ->
            dialog.dismiss()
        }
        mBuilder.create().show()
    }
}