package ru.iabarmin.todoapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import ru.iabarmin.todoapp.R
import ru.iabarmin.todoapp.TaskViewModel
import ru.iabarmin.todoapp.data.Task
import ru.iabarmin.todoapp.databinding.FragmentAddBinding

class AddFragment : Fragment() {
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentAddBinding.inflate(inflater)

        val myAdapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.priorities)
        )

        binding.btnBackAdd.setOnClickListener {
            findNavController().navigate(AddFragmentDirections.actionAddFragmentToTaskFragment())
        }

        binding.apply {
            btnAddTask.isEnabled = false
            spinner.adapter = myAdapter

            /**
             * Если поле ввода пустое, то кнопка добавления неактивна
             */
            editTask.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
                override fun afterTextChanged(p0: Editable?) { }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    btnAddTask.isEnabled = editTask.text.toString().isNotBlank()
                }
            })

            btnAddTask.setOnClickListener {
                val title = editTask.text.toString()
                val priority = spinner.selectedItemPosition

                val task = Task(
                    0,
                    title,
                    priority,
                    System.currentTimeMillis()
                )
                viewModel.insert(task)
                Toast.makeText(activity, "Задача добавлена!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addFragment_to_taskFragment)
            }

            btnBackAdd.setOnClickListener {
                findNavController().navigate(AddFragmentDirections.actionAddFragmentToTaskFragment())
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }
}