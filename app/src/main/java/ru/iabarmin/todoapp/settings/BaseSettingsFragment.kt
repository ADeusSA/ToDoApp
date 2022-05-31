package ru.iabarmin.todoapp.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.iabarmin.todoapp.R
import ru.iabarmin.todoapp.databinding.FragmentBaseSettingsBinding

class BaseSettingsFragment : Fragment() {

    lateinit var binding: FragmentBaseSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseSettingsBinding.inflate(inflater)


        return binding.root
    }

}