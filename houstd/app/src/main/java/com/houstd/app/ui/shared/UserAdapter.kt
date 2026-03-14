package com.houstd.app.ui.shared

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.houstd.app.R
import com.houstd.app.data.model.Role
import com.houstd.app.data.model.User

class UserAdapter(
    private val currentUserId: String,
    private val onDelete: (String) -> Unit
) : ListAdapter<User, UserAdapter.VH>(DIFF) {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvInitial: TextView = view.findViewById(R.id.tvInitial)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvEmail: TextView = view.findViewById(R.id.tvEmail)
        val tvRole: TextView = view.findViewById(R.id.tvRole)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val user = getItem(position)
        holder.tvInitial.text = user.name.first().toString()
        holder.tvName.text = user.name
        holder.tvEmail.text = user.email
        holder.tvRole.text = user.role.name.lowercase().replaceFirstChar { it.uppercase() }

        val (bgColor, textColor) = when (user.role) {
            Role.ADMIN -> R.color.role_admin_bg to R.color.role_admin_text
            Role.OWNER -> R.color.role_owner_bg to R.color.role_owner_text
            Role.STUDENT -> R.color.role_student_bg to R.color.role_student_text
        }
        holder.tvRole.backgroundTintList = holder.itemView.context.getColorStateList(bgColor)
        holder.tvRole.setTextColor(holder.itemView.context.getColor(textColor))
        holder.tvInitial.backgroundTintList = holder.itemView.context.getColorStateList(bgColor)

        holder.btnDelete.visibility = if (user.id == currentUserId) View.GONE else View.VISIBLE
        holder.btnDelete.setOnClickListener { onDelete(user.id) }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(a: User, b: User) = a.id == b.id
            override fun areContentsTheSame(a: User, b: User) = a == b
        }
    }
}
