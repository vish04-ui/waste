package com.example.wastemanagement.ui.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.wastemanagement.R
import com.example.wastemanagement.ui.fragments.HomeFragment
import com.example.wastemanagement.ui.fragments.ProfileFragment
import com.example.wastemanagement.ui.fragments.RecyclingGuideFragment
import com.example.wastemanagement.ui.fragments.WasteCollectionFragment

class MainActivity: AppCompatActivity() {
    private lateinit var navHome: LinearLayout
    private lateinit var navCollection: LinearLayout
    private lateinit var navRecycling: LinearLayout
    private lateinit var navProfile: LinearLayout
    private lateinit var iconHome: ImageView
    private lateinit var iconCollection: ImageView
    private lateinit var iconRecycling: ImageView
    private lateinit var iconProfile: ImageView
    private lateinit var labelHome: TextView
    private lateinit var labelCollection: TextView
    private lateinit var labelRecycling: TextView
    private lateinit var labelProfile: TextView
    private var currentTag: String = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bind()
        if (savedInstanceState == null) select("home") else restoreSelection()
        navHome.setOnClickListener { select("home") }
        navCollection.setOnClickListener { select("collection") }
        navRecycling.setOnClickListener { select("recycling") }
        navProfile.setOnClickListener { select("profile") }
    }
    private fun bind(){
        navHome = findViewById(R.id.navHome)
        navCollection = findViewById(R.id.navCollection)
        navRecycling = findViewById(R.id.navRecycling)
        navProfile = findViewById(R.id.navProfile)
        iconHome = findViewById(R.id.iconHome)
        iconCollection = findViewById(R.id.iconCollection)
        iconRecycling = findViewById(R.id.iconRecycling)
        iconProfile = findViewById(R.id.iconProfile)
        labelHome = findViewById(R.id.labelHome)
        labelCollection = findViewById(R.id.labelCollection)
        labelRecycling = findViewById(R.id.labelRecycling)
        labelProfile = findViewById(R.id.labelProfile)
    }
    private fun select(tag: String){
        if (currentTag == tag) return
        currentTag = tag
        val fragment: Fragment = when(tag){
            "home" -> HomeFragment()
            "collection" -> WasteCollectionFragment()
            "recycling" -> RecyclingGuideFragment()
            "profile" -> ProfileFragment()
            else -> HomeFragment()
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment, tag)
            .commit()
        updateNav()
    }
    private fun restoreSelection(){
        // Rebuild labels/colors/icons
        updateNav()
    }
    private fun updateNav(){
        val pairs = listOf(
            Triple("home", navHome to iconHome, labelHome),
            Triple("collection", navCollection to iconCollection, labelCollection),
            Triple("recycling", navRecycling to iconRecycling, labelRecycling),
            Triple("profile", navProfile to iconProfile, labelProfile)
        )
        pairs.forEach { (tag, viewPair, label) ->
            val (container, icon) = viewPair
            val selected = tag == currentTag
            container.isSelected = selected
            label.isSelected = selected
            icon.setImageResource(when(tag){
                "home" -> if(selected) R.drawable.ic_home_selected else R.drawable.ic_home
                "collection" -> if(selected) R.drawable.ic_collection_selected else R.drawable.ic_collection
                "recycling" -> if(selected) R.drawable.ic_recycle_selected else R.drawable.ic_recycle
                "profile" -> if(selected) R.drawable.ic_profile_selected else R.drawable.ic_profile
                else -> R.drawable.ic_home
            })
            label.alpha = if(selected) 1f else 0.75f
        }
    }
}
