package com.example.a

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotificationAdapter(
    private val items: MutableList<Triple<Int, String, String>>,
    private val onDelete: (Int, Int) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDisease: TextView = itemView.findViewById(R.id.tvNotifDisease)
        val tvDetail: TextView = itemView.findViewById(R.id.tvNotifDetail)
        val btnDelete: Button = itemView.findViewById(R.id.btnDeleteNotif)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (id, disease, detail) = items[position]
        holder.tvDisease.text = disease
        holder.tvDetail.text = detail
        holder.btnDelete.setOnClickListener { onDelete(id, id) }
    }

    override fun getItemCount() = items.size
}