package com.example.wastemanagement.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wastemanagement.R
import com.example.wastemanagement.ui.fragments.dashboard.FeatureGridAdapter
import com.example.wastemanagement.ui.fragments.dashboard.FeatureItem
import com.example.wastemanagement.ui.localization.LanguageManager
import com.example.wastemanagement.ui.localization.AppLanguage
import com.example.wastemanagement.ui.theme.ThemeManager
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.google.android.material.card.MaterialCardView
import com.google.android.material.button.MaterialButton
import androidx.appcompat.app.AlertDialog

class HomeFragment : Fragment() {
    private lateinit var languageManager: LanguageManager
    private lateinit var themeManager: ThemeManager
    private lateinit var languageCard: MaterialCardView
    private lateinit var themeToggleButton: MaterialButton
    private lateinit var languageCodeText: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        languageManager = LanguageManager(requireContext())
        themeManager = ThemeManager(requireContext())
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_home, container, false)
        setupToolbar(v)
        setupWelcome(v)
        setupStats(v)
        setupFeatureGrid(v)
        return v
    }

    private fun setupToolbar(root: View) {
        languageCard = root.findViewById(R.id.languageCard)
        themeToggleButton = root.findViewById(R.id.themeToggleButton)
        languageCodeText = root.findViewById(R.id.languageCode)
        
        // Set up language selector
        languageCard.setOnClickListener {
            showLanguageSelector()
        }
        
        // Set up theme toggle
        themeToggleButton.setOnClickListener {
            lifecycleScope.launch {
                themeManager.toggleTheme()
                // Recreate activity to apply theme change
                requireActivity().recreate()
            }
        }
        
        // Observe language changes
        lifecycleScope.launch {
            languageManager.currentLanguage.collectLatest { language ->
                languageCodeText.text = language.code.uppercase()
            }
        }
        
        // Observe theme changes
        lifecycleScope.launch {
            themeManager.isDarkMode.collectLatest { isDark ->
                val iconRes = if (isDark) R.drawable.ic_light_mode else R.drawable.ic_dark_mode
                themeToggleButton.setIconResource(iconRes)
            }
        }
    }
    
    private fun showLanguageSelector() {
        val languages = AppLanguage.values()
        val languageNames = languages.map { lang ->
            when (lang) {
                AppLanguage.ENGLISH -> "English"
                AppLanguage.SINHALA -> "සිංහල (Sinhala)"
                AppLanguage.TAMIL -> "தமிழ் (Tamil)"
            }
        }.toTypedArray()
        
        var currentLanguage: AppLanguage = AppLanguage.ENGLISH
        lifecycleScope.launch {
            languageManager.currentLanguage.collectLatest { lang ->
                currentLanguage = lang
            }
        }
        
        val currentIndex = languages.indexOf(currentLanguage)
        
        AlertDialog.Builder(requireContext())
            .setTitle("Select Language")
            .setSingleChoiceItems(languageNames, currentIndex) { dialog, which ->
                lifecycleScope.launch {
                    languageManager.setLanguage(languages[which])
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun setupWelcome(root: View) {
        val title = root.findViewById<TextView>(R.id.welcomeTitle)
        val subtitle = root.findViewById<TextView>(R.id.welcomeSubtitle)
        // initial
        var currentLang = AppLanguage.ENGLISH
        title.text = languageManager.getLocalizedStringForLanguage(currentLang, "welcome_title")
        subtitle.text = languageManager.getLocalizedStringForLanguage(currentLang, "welcome_subtitle")
        viewLifecycleOwner.lifecycleScope.launch {
            languageManager.currentLanguage.collectLatest { lang ->
                currentLang = lang
                title.text = languageManager.getLocalizedStringForLanguage(lang, "welcome_title")
                subtitle.text = languageManager.getLocalizedStringForLanguage(lang, "welcome_subtitle")
                setupStats(root)
                setupFeatureGrid(root)
            }
        }
    }
    private fun setupStats(root: View) {
        val row = root.findViewById<LinearLayout>(R.id.statsRow)
        row.removeAllViews()
        val lang = AppLanguage.ENGLISH // will update via collector above
        val stats = listOf(
            Triple(R.drawable.ic_check_circle, languageManager.getLocalizedStringForLanguage(lang, "collections"), "12"),
            Triple(R.drawable.ic_autorenew, languageManager.getLocalizedStringForLanguage(lang, "recycled"), "85%")
        )
        stats.forEach { (iconRes, title, value) ->
            val card = layoutInflater.inflate(R.layout.item_stat_card, row, false)
            val icon = card.findViewById<ImageView>(R.id.icon)
            val valueTv = card.findViewById<TextView>(R.id.value)
            val titleTv = card.findViewById<TextView>(R.id.title)
            icon.setImageResource(iconRes)
            valueTv.text = value
            titleTv.text = title
            val params = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            params.setMargins(8, 0, 8, 0)
            row.addView(card, params)
        }
    }
    private fun setupFeatureGrid(root: View) {
        val recycler = root.findViewById<RecyclerView>(R.id.featureGrid)
        recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        val lang = AppLanguage.ENGLISH
        val items = listOf(
            FeatureItem("waste_collection", R.drawable.ic_collection, languageManager.getLocalizedStringForLanguage(lang, "waste_collection"), languageManager.getLocalizedStringForLanguage(lang, "waste_collection_desc")) { /* TODO navigate */ },
            FeatureItem("recycling_guide", R.drawable.ic_recycle, languageManager.getLocalizedStringForLanguage(lang, "recycling_guide"), languageManager.getLocalizedStringForLanguage(lang, "recycling_guide_desc")) { },
            FeatureItem("profile", R.drawable.ic_person, languageManager.getLocalizedStringForLanguage(lang, "profile"), languageManager.getLocalizedStringForLanguage(lang, "profile_desc")) { },
            FeatureItem("history", R.drawable.ic_info, languageManager.getLocalizedStringForLanguage(lang, "history"), languageManager.getLocalizedStringForLanguage(lang, "history_desc")) { }
        )
        recycler.adapter = FeatureGridAdapter(items)
    }
}
