package com.app.studentmanagementsystem.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.studentmanagementsystem.databinding.BottomSheetDialogLayoutBinding
import com.app.studentmanagementsystem.databinding.ItemStudentBinding
import com.app.studentmanagementsystem.models.User
import com.app.studentmanagementsystem.models.Users
import com.google.android.material.bottomsheet.BottomSheetDialog

class StudentAdapter(
    private val context: Context,
    private val studentList: Users,
    private var onUpdate: ((user: User) -> Unit)? = null,
    private var onDelete: ((id: String) -> Unit)? = null
) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(context), parent, false)
        return StudentViewHolder(binding)
    }

    override fun getItemCount(): Int = studentList.student.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList.student[position]

        holder.binding.tvName.text = student.name
        holder.binding.tvEmail.text = student.email
        holder.binding.tvMobile.text = student.mobile

        holder.binding.ivUpdate.setOnClickListener {
            onUpdate?.invoke(student)
        }

        holder.binding.ivDelete.setOnClickListener {
            onDelete?.invoke(student.id)
        }

        holder.binding.studentItem.setOnClickListener {
            val dialog = BottomSheetDialog(context)
            val dialogBinding = BottomSheetDialogLayoutBinding.inflate(LayoutInflater.from(context))
            dialog.setContentView(dialogBinding.root)
            dialogBinding.tvName.text = student.name
            dialogBinding.tvEmail.text = student.email
            dialogBinding.tvMobile.text = student.mobile
            dialog.show()
        }
    }

    // Update student data and notify
    fun updateStudent(position: Int, updatedStudent: User) {
        studentList.student[position] = updatedStudent
        notifyItemChanged(position)
    }

    // Remove student data and notify
    fun removeStudent(position: Int) {
        studentList.student.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun getPosition(studentId: String): Int {
        return studentList.student.indexOfFirst { it.id == studentId }
    }
}