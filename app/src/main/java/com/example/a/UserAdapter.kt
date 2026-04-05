package com.example.a

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(
    private val users: MutableList<Triple<String, String, String>>,
    private val onDelete: (String) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvUserName)
        val tvEmail: TextView = itemView.findViewById(R.id.tvUserEmail)
        val btnDelete: Button = itemView.findViewById(R.id.btnDeleteUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val (username, email, _) = users[position]
        holder.tvName.text = username
        holder.tvEmail.text = email
        holder.btnDelete.setOnClickListener { onDelete(username) }
    }

    override fun getItemCount() = users.size
}