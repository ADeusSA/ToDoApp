package ru.iabarmin.todoapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.iabarmin.todoapp.R
import ru.iabarmin.todoapp.TaskViewModel
import ru.iabarmin.todoapp.data.Task
import ru.iabarmin.todoapp.databinding.FragmentUpdateBinding

class UpdateFragment : Fragment() {

    private val viewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentUpdateBinding.inflate(inflater)
        var args = UpdateFragmentArgs.fromBundle(requireArguments())

        binding.apply {
            editTaskUpdate.setText(args.task.title)
            spinnerUpdate.setSelection(args.task.priority)

            /**
             * Если поле ввода пустое, то кнопка изменения неактивна
             */
            editTaskUpdate.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
                override fun afterTextChanged(p0: Editable?) { }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    btnUpdateTask.isEnabled = editTaskUpdate.text.toString().isNotBlank()
                }

            })

            btnUpdateTask.setOnClickListener {
                if (TextUtils.isEmpty(editTaskUpdate.text)) {
                    Toast.makeText(activity,"Введите название задачи!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val task_str = editTaskUpdate.text.toString()
                val priority = spinnerUpdate.selectedItemPosition

                val task = Task(
                    args.task.id,
                    task_str,
                    priority,
                    args.task.timestamp
                )

                viewModel.update(task)
                Toast.makeText(activity, "Успешно изменено!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_updateFragment_to_taskFragment)
            }

            btnBackUpdate.setOnClickListener {
                findNavController().navigate(UpdateFragmentDirections.actionUpdateFragmentToTaskFragment())
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }
}