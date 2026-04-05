package com.example.a

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DiseaseAdapter(
    private val diseases: MutableList<Pair<String, String>>,
    private val onEdit: (Pair<String, String>) -> Unit,
    private val onDelete: (String) -> Unit
) : RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder>() {

    inner class DiseaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvDiseaseName)
        val tvDesc: TextView = itemView.findViewById(R.id.tvDiseaseDesc)
        val btnEdit: Button = itemView.findViewById(R.id.btnEditDisease)
        val btnDelete: Button = itemView.findViewById(R.id.btnDeleteDisease)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiseaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_disease, parent, false)
        return DiseaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiseaseViewHolder, position: Int) {
        val (name, desc) = diseases[position]
        holder.tvName.text = name
        holder.tvDesc.text = desc
        holder.btnEdit.setOnClickListener { onEdit(diseases[position]) }
        holder.btnDelete.setOnClickListener { onDelete(name) }
    }

    override fun getItemCount() = diseases.size
}