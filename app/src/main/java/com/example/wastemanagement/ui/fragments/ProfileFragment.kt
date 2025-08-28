package com.example.wastemanagement.ui.fragments

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.wastemanagement.R
import com.example.wastemanagement.ui.activities.LoginActivity
import com.example.wastemanagement.ui.auth.AuthManager
import com.example.wastemanagement.ui.localization.AppLanguage
import com.example.wastemanagement.ui.localization.LanguageManager
import com.example.wastemanagement.ui.theme.ThemeManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val s1 = "Bimsara Bandara"

class ProfileFragment : Fragment() {
    private lateinit var languageManager: LanguageManager
    private lateinit var themeManager: ThemeManager
    private lateinit var authManager: AuthManager

    private var notificationsEnabled = true
    private var autoScheduleEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx = requireContext().applicationContext
        languageManager = LanguageManager(ctx)
        themeManager = ThemeManager(ctx)
        authManager = AuthManager(ctx)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val containerLayout: LinearLayout = view.findViewById(R.id.container)
        viewLifecycleOwner.lifecycleScope.launch {
            languageManager.currentLanguage.collectLatest { lang ->
                buildUi(containerLayout, lang)
            }
        }
    }

    private fun buildUi(root: LinearLayout, lang: AppLanguage) {
        root.removeAllViews()
        val ctx = root.context
        fun addSpacing(h: Int) { View(ctx).apply { layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h) }.also(root::addView) }
        
        root.setPadding(24, 32, 24, 56)
        
        // Header Card
        root.addView(buildProfileHeader(ctx, lang))
        addSpacing(40)
        
        // Settings title
        root.addView(TextView(ctx).apply {
            text = languageManager.getLocalizedStringForLanguage(lang, "settings")
            textSize = 22f
            setTypeface(typeface, Typeface.BOLD)
            setPadding(8, 0, 0, 0)
        })
        addSpacing(24)
        
        root.addView(buildToggleCard(ctx, languageManager.getLocalizedStringForLanguage(lang, "notifications"), languageManager.getLocalizedStringForLanguage(lang, "notifications_desc")) { switch ->
            switch.isChecked = notificationsEnabled
            switch.setOnCheckedChangeListener { _, b -> notificationsEnabled = b }
        })
        addSpacing(20)
        
        root.addView(buildThemeCard(ctx, lang))
        addSpacing(20)
        
        root.addView(buildToggleCard(ctx, languageManager.getLocalizedStringForLanguage(lang, "auto_schedule"), languageManager.getLocalizedStringForLanguage(lang, "auto_schedule_desc")) { switch ->
            switch.isChecked = autoScheduleEnabled
            switch.setOnCheckedChangeListener { _, b -> autoScheduleEnabled = b }
        })
        addSpacing(40)
        
        root.addView(TextView(ctx).apply {
            text = languageManager.getLocalizedStringForLanguage(lang, "account")
            textSize = 22f
            setTypeface(typeface, Typeface.BOLD)
            setPadding(8, 0, 0, 0)
        })
        addSpacing(24)
        
        val actions = listOf(
            "edit_profile" to "edit_profile_desc",
            "address_settings" to "address_settings_desc",
            "help_support" to "help_support_desc",
            "about" to "about_desc"
        )
        actions.forEachIndexed { i, (t, s) ->
            root.addView(buildActionCard(ctx, languageManager.getLocalizedStringForLanguage(lang, t), languageManager.getLocalizedStringForLanguage(lang, s)))
            if (i < actions.size - 1) addSpacing(20)
        }
        addSpacing(40)
        
        root.addView(buildLogoutButton(ctx, lang))
    }

    private fun buildProfileHeader(ctx: android.content.Context, lang: AppLanguage): View {
        val card = layoutInflater.inflate(R.layout.profile_header_card, null)
        val name = card.findViewById<TextView>(R.id.textName)
        val email = card.findViewById<TextView>(R.id.textEmail)
        val stat1 = card.findViewById<TextView>(R.id.textStat1Value)
        val stat1Label = card.findViewById<TextView>(R.id.textStat1Label)
        val stat2 = card.findViewById<TextView>(R.id.textStat2Value)
        val stat2Label = card.findViewById<TextView>(R.id.textStat2Label)
        val stat3 = card.findViewById<TextView>(R.id.textStat3Value)
        val stat3Label = card.findViewById<TextView>(R.id.textStat3Label)
        name.text = "Bimsara Bandara"
        email.text = "banda@email.com"
        stat1.text = "24"; stat2.text = "156 kg"; stat3.text = "15 days"
        stat1Label.text = languageManager.getLocalizedStringForLanguage(lang, "collections")
        stat2Label.text = languageManager.getLocalizedStringForLanguage(lang, "recycled")
        stat3Label.text = languageManager.getLocalizedStringForLanguage(lang, "streak")
        return card
    }

    private fun buildToggleCard(ctx: android.content.Context, title: String, subtitle: String, configure: (Switch) -> Unit): View {
        val card = layoutInflater.inflate(R.layout.settings_toggle_card, null) as CardView
        val titleTv = card.findViewById<TextView>(R.id.textTitle)
        val subtitleTv = card.findViewById<TextView>(R.id.textSubtitle)
        val switch = card.findViewById<Switch>(R.id.toggleSwitch)
        titleTv.text = title
        subtitleTv.text = subtitle
        configure(switch)
        return card
    }

    private fun buildThemeCard(ctx: android.content.Context, lang: AppLanguage): View {
        val card = layoutInflater.inflate(R.layout.settings_toggle_card, null) as CardView
        val titleTv = card.findViewById<TextView>(R.id.textTitle)
        val subtitleTv = card.findViewById<TextView>(R.id.textSubtitle)
        val switch = card.findViewById<Switch>(R.id.toggleSwitch)
        titleTv.text = languageManager.getLocalizedStringForLanguage(lang, "dark_mode")
        subtitleTv.text = languageManager.getLocalizedStringForLanguage(lang, "dark_mode_desc")
        viewLifecycleOwner.lifecycleScope.launch {
            themeManager.isDarkMode.collectLatest { isDark -> switch.isChecked = isDark }
        }
        switch.setOnCheckedChangeListener { _, b ->
            viewLifecycleOwner.lifecycleScope.launch { themeManager.setDarkMode(b) }
        }
        return card
    }

    private fun buildActionCard(ctx: android.content.Context, title: String, subtitle: String): View {
        val card = layoutInflater.inflate(R.layout.settings_action_card, null) as CardView
        card.findViewById<TextView>(R.id.textTitle).text = title
        card.findViewById<TextView>(R.id.textSubtitle).text = subtitle
        return card
    }

    private fun buildLogoutButton(ctx: android.content.Context, lang: AppLanguage): View {
        val container = LinearLayout(ctx).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            setPadding(0,16,0,0)
        }
        val space = View(ctx).apply { layoutParams = LinearLayout.LayoutParams(0, 0, 1f) }
        val btn = com.google.android.material.button.MaterialButton(ctx).apply {
            text = languageManager.getLocalizedStringForLanguage(lang, "logout")
            icon = ctx.getDrawable(R.drawable.ic_check)
            setIconTintResource(android.R.color.white)
            setBackgroundColor(ctx.getColor(R.color.accent_red))
            setTextColor(android.graphics.Color.WHITE)
            cornerRadius = 50
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            setPadding(40,20,40,20)
            elevation = 6f
            setOnClickListener { 
                viewLifecycleOwner.lifecycleScope.launch { 
                    authManager.logout()
                    // Navigate to LoginActivity
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    requireActivity().finish()
                } 
            }
        }
        container.addView(space)
        container.addView(btn)
        return container
    }
}

