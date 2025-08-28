package com.example.wastemanagement.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.wastemanagement.ui.localization.LanguageManager
import com.example.wastemanagement.ui.theme.EcoGridTheme
import com.example.wastemanagement.ui.theme.ThemeManager

/** Simple base fragment hosting a single ComposeContent composable inside app theme. */
abstract class BaseComposeFragment : Fragment() {
    abstract val screenContent: @Composable () -> Unit

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            EcoGridTheme(themeManager = ThemeManager(requireContext())) {
                screenContent()
            }
        }
    }
}
