package com.example.a1week

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.a1week.databinding.ItemListBinding

data class Todo(
    var text: String,
    var isDone: Boolean = false,
    var isCheckBoxVisible: Boolean = false
)
class TodoAdapter(
    private val dataSet: List<Todo>,
//추가시작
    private val onClickDeleteIcon: (todo: Todo) -> Unit, //2. delete button이 눌렸을때 onclickDeleteIcon을 실행하라는뜻, 0->Unit이기때문에 함수자체에 return없다는뜻

//추가 끝
) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: Todo) {
            binding.todoTitle.text = todo.text

            if (todo.isCheckBoxVisible) {
                binding.checkBox.visibility = View.VISIBLE
            } else {
                binding.checkBox.visibility = View.INVISIBLE
            }
        }
//        val todoTitle: TextView
//            get() {
//                TODO()
//            }
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_list, viewGroup, false)

        return TodoViewHolder(ItemListBinding.bind(view))
    }

    override fun onBindViewHolder(todoViewHolder: TodoViewHolder, position: Int) {//item을 화면에 표시해주는
        val todo = dataSet[position]

        todoViewHolder.bind(todo)
        todoViewHolder.binding.todoTitle.text = todo.text

        if (todo.isCheckBoxVisible) {
            todoViewHolder.binding.checkBox.visibility = View.VISIBLE
        } else {
            todoViewHolder.binding.checkBox.visibility = View.INVISIBLE
        }

        todoViewHolder.binding.root.setOnClickListener {
            if (todo.isCheckBoxVisible) {
                todo.isCheckBoxVisible = false
                notifyItemChanged(position)
            }
            else {
                todo.isCheckBoxVisible = true
                notifyItemChanged(position)
            }
        }

        todoViewHolder.binding.root.setOnLongClickListener {
            showEditDialog(todoViewHolder.binding.root.context, todo)
            true
        }
//추가시작
        todoViewHolder.binding.deleteImage.setOnClickListener {
            onClickDeleteIcon.invoke(todo) //1. deleteimage가 눌렸을때 listposition를 전달하면서 onClickDeleteIcon함수를 실행한다.
        }
//추가끝
    }

    @SuppressLint("MissingInflatedId")
    private fun showEditDialog(context: Context, todo: Todo) {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_edit_todo, null)
        val editText = dialogView.findViewById<EditText>(R.id.editList)
        editText.setText(todo.text)

        dialogBuilder.setView(dialogView)
            .setTitle("할 일 수정")
            .setPositiveButton("확인") { dialog, _ ->
                val updatedText = editText.text.toString()
                todo.text = updatedText
                notifyItemChanged(dataSet.indexOf(todo))
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun getItemCount() = dataSet.size
    }