package com.example.wastemanagement.ui.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.wastemanagement.R

class SimpleTextPagerAdapter(
    private val context: android.content.Context,
    private val pages: List<Pair<String,String>>
) : RecyclerView.Adapter<SimpleTextPagerAdapter.Holder>() {
    var useRichLayout: Boolean = false
    inner class Holder(v: View): RecyclerView.ViewHolder(v){
        val title: TextView = v.findViewById(R.id.textTitle)
        val desc: TextView = v.findViewById(R.id.textDesc)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(context).inflate(R.layout.item_onboarding_page, parent, false))
    }
    override fun getItemCount(): Int = pages.size
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.title.text = pages[position].first
        holder.desc.text = pages[position].second
    }
}
