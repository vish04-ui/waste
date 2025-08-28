package com.example.wastemanagement.ui.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.wastemanagement.R

class SimpleTextPagerAdapter(
    private val context: android.content.Context,
    private val pages: List<Pair<String,String>>
) : RecyclerView.Adapter<SimpleTextPagerAdapter.Holder>() {
    var useRichLayout: Boolean = false
    
    // Array of onboarding images corresponding to each page
    private val onboardingImages = arrayOf(
        R.drawable.onboarding1,
        R.drawable.onboarding2,
        R.drawable.onboarding3
    )
    
    // Array of icons corresponding to each page theme
    private val onboardingIcons = arrayOf(
        R.drawable.ic_collection,
        R.drawable.ic_recycle,
        R.drawable.ic_eco
    )
    
    inner class Holder(v: View): RecyclerView.ViewHolder(v){
        val title: TextView = v.findViewById(R.id.textTitle)
        val desc: TextView = v.findViewById(R.id.textDesc)
        val imageOnboarding: ImageView = v.findViewById(R.id.imageOnboarding)
        val icon: ImageView = v.findViewById(R.id.icon)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(context).inflate(R.layout.item_onboarding_page, parent, false))
    }
    override fun getItemCount(): Int = pages.size
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.title.text = pages[position].first
        holder.desc.text = pages[position].second
        
        // Set the appropriate onboarding image for this page
        if (position < onboardingImages.size) {
            holder.imageOnboarding.setImageResource(onboardingImages[position])
        }
        
        // Set the appropriate icon for this page
        if (position < onboardingIcons.size) {
            holder.icon.setImageResource(onboardingIcons[position])
        }
    }
}
