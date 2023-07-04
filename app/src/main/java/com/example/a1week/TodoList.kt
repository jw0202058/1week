package com.example.a1week

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a1week.databinding.FragmentTodoBinding
import com.google.gson.Gson

class TodoList : Fragment() {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!
    private val data = arrayListOf<Todo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터 초기화
        loadData()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = TodoAdapter(data,
            onClickDeleteIcon =
                ::deleteTask,
            saveData = ::saveData
        )

        binding.addButton.setOnClickListener {
            addTask()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addTask() {
        val todo = Todo(binding.editText.text.toString(), isDone=false)
        data.add(todo)
        binding.recyclerView.adapter?.notifyDataSetChanged()
        binding.editText.text = null            // 입력창 초기화

        saveData(data) // 데이터를 내부 저장소에 저장
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteTask(todo: Todo) {
        val position = data.indexOf(todo)
        if (position != -1) {
            data.removeAt(position)
            binding.recyclerView.adapter?.notifyItemRemoved(position)

            saveData(data) // 삭제된 데이터를 내부 저장소에 저장
        }
    }
    fun saveData(todoList: List<Todo>) {
        val sharedPreferences = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Todo 리스트를 JSON 형태로 변환하여 저장
        val json = Gson().toJson(todoList)
        editor.putString("todo_list", json)
        editor.apply()
    }
    private fun loadData() {
        val sharedPreferences = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("todo_list", null)

        // 저장된 데이터가 있는 경우에만 처리
        if (!json.isNullOrEmpty()) {
            val todoList = Gson().fromJson(json, Array<Todo>::class.java).toList()
            data.addAll(todoList)
        }
    }
}
