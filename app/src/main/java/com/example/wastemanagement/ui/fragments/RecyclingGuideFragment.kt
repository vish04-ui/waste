package com.example.wastemanagement.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wastemanagement.R
import com.example.wastemanagement.ui.localization.LanguageManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RecyclingGuideFragment : Fragment() {

    private lateinit var languageManager: LanguageManager
    private lateinit var adapter: RecyclingGuideAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Simple retrieval via requireContext(); DI placeholder
        languageManager = LanguageManager(requireContext().applicationContext)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_recycling_guide, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler: RecyclerView = view.findViewById(R.id.recyclerView)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        adapter = RecyclingGuideAdapter(languageManager, com.example.wastemanagement.ui.localization.AppLanguage.ENGLISH)
        recycler.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            languageManager.currentLanguage.collectLatest { lang ->
                adapter.rebuild(lang)
            }
        }
        adapter.rebuild(com.example.wastemanagement.ui.localization.AppLanguage.ENGLISH)
    }
}

