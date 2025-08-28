package com.example.wastemanagement.ui.fragments.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wastemanagement.R

data class FeatureItem(
    val id: String,
    val iconRes: Int,
    val title: String,
    val description: String,
    val onClick: () -> Unit
)

class FeatureGridAdapter(private val items: List<FeatureItem>) : RecyclerView.Adapter<FeatureGridAdapter.Holder>() {
    inner class Holder(v: View) : RecyclerView.ViewHolder(v) {
        val icon: ImageView = v.findViewById(R.id.icon)
        val title: TextView = v.findViewById(R.id.title)
        val desc: TextView = v.findViewById(R.id.description)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feature_card, parent, false)
        return Holder(view)
    }
    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        holder.icon.setImageResource(item.iconRes)
        holder.title.text = item.title
        holder.desc.text = item.description
        holder.itemView.setOnClickListener { item.onClick() }
    }
}
