package ru.iabarmin.todoapp.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.iabarmin.todoapp.R
import ru.iabarmin.todoapp.TaskViewModel
import ru.iabarmin.todoapp.databinding.FragmentTaskBinding
import ru.iabarmin.todoapp.util.TaskAdapter
import ru.iabarmin.todoapp.util.TaskClickListener
import java.sql.Struct

class TaskFragment : Fragment() {
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentTaskBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        adapter = TaskAdapter(TaskClickListener { task ->
            findNavController().navigate(TaskFragmentDirections.actionTaskFragmentToUpdateFragment(task))
        })

        viewModel.getAllTasks.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        binding.apply {
            binding.recyclerView.adapter = adapter

            floatingActionButton.setOnClickListener {
                findNavController().navigate(R.id.action_taskFragment_to_addFragment)
            }
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
        ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val task = adapter.currentList[position]
                viewModel.delete(task)

                Snackbar.make(binding.root, "Успешно удалено!", Snackbar.LENGTH_LONG).apply {
                    setAction("Отмена"){
                        viewModel.insert(task)
                    }
                    show()
                }
            }

        }).attachToRecyclerView(binding.recyclerView)

        setHasOptionsMenu(true)

        hideKeyboard(requireActivity())
        return binding.root
    }

    private fun hideKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = activity.currentFocus
        currentFocusedView.let {
            inputMethodManager.hideSoftInputFromWindow(
                currentFocusedView?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.task_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null) {
                    runQuery(newText)
                }
                return true
            }

        })
    }

    fun runQuery(query: String) {
        val searchQuery = "%$query%"
        viewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner, Observer { tasks ->
            adapter.submitList(tasks)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_priority -> viewModel.getAllPriorityTasks.observe(viewLifecycleOwner,
                Observer { tasks -> adapter.submitList(tasks) })
            R.id.action_deleteAll -> deleteAllItem()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllItem() {
        AlertDialog.Builder(requireContext())
            .setTitle("Удалить все задачи")
            .setMessage("Вы уверены?")
            .setPositiveButton("Да"){dialog, _ ->
                viewModel.deleteAll()
                dialog.dismiss()
            }.setNegativeButton("Нет"){dialog, _ ->
                dialog.dismiss()
            }.create().show()
    }

}