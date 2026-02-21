package com.example.houstd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ApartmentAdapter(private val apartmentList: List<String>) :
    RecyclerView.Adapter<ApartmentAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.apartmentTitle)
        val viewBtn: Button = itemView.findViewById(R.id.viewDetailsBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_apartment, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = apartmentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = apartmentList[position]
    }
}