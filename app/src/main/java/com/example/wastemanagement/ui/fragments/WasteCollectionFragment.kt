package com.example.wastemanagement.ui.fragments

import android.app.AlertDialog
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wastemanagement.R
import com.example.wastemanagement.ui.localization.AppLanguage
import com.example.wastemanagement.ui.localization.LanguageManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WasteCollectionFragment : Fragment() {

    private lateinit var languageManager: LanguageManager
    private lateinit var adapter: WasteCollectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        languageManager = LanguageManager(requireContext().applicationContext)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_waste_collection, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler: RecyclerView = view.findViewById(R.id.recyclerView)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        adapter = WasteCollectionAdapter(languageManager, AppLanguage.ENGLISH)
        recycler.adapter = adapter
        val fab: FloatingActionButton = view.findViewById(R.id.fabSchedule)
        fab.setOnClickListener { showScheduleDialog() }
        viewLifecycleOwner.lifecycleScope.launch {
            languageManager.currentLanguage.collectLatest { lang ->
                adapter.rebuild(lang)
            }
        }
        adapter.rebuild(AppLanguage.ENGLISH)
    }

    private fun showScheduleDialog() {
        val ctx = requireContext()
        val dialogView = layoutInflater.inflate(R.layout.dialog_schedule_collection, null)
        val title = dialogView.findViewById<TextView>(R.id.textDialogTitle)
        val selectDateLabel = dialogView.findViewById<TextView>(R.id.textSelectDateLabel)
        val wasteTypeLabel = dialogView.findViewById<TextView>(R.id.textWasteTypeLabel)
        val btnPrev = dialogView.findViewById<ImageView>(R.id.buttonPrevDay)
        val btnNext = dialogView.findViewById<ImageView>(R.id.buttonNextDay)
        val dateText = dialogView.findViewById<TextView>(R.id.textSelectedDate)
        val wasteTypeContainer = dialogView.findViewById<LinearLayout>(R.id.wasteTypeContainer)
        val cancel = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.buttonCancel)
        val schedule = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.buttonSchedule)
        var selectedDate = LocalDate.now()
        var selectedWasteType: String? = null
        fun fmtDate() { dateText.text = selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) }
        fmtDate()
        val lang = AppLanguage.ENGLISH // snapshot; dynamic update skipped for brevity
        title.text = languageManager.getLocalizedStringForLanguage(lang, "schedule_collection_title")
        selectDateLabel.text = languageManager.getLocalizedStringForLanguage(lang, "select_date")
        wasteTypeLabel.text = languageManager.getLocalizedStringForLanguage(lang, "waste_type")
        val wasteTypes = listOf(
            languageManager.getLocalizedStringForLanguage(lang, "waste_type_general"),
            languageManager.getLocalizedStringForLanguage(lang, "waste_type_recyclables"),
            languageManager.getLocalizedStringForLanguage(lang, "waste_type_organic"),
            languageManager.getLocalizedStringForLanguage(lang, "waste_type_hazardous"),
            languageManager.getLocalizedStringForLanguage(lang, "waste_type_electronics")
        )
        val radioGroup = RadioGroup(ctx)
        radioGroup.orientation = RadioGroup.VERTICAL
        wasteTypes.forEach { wt ->
            val rb = RadioButton(ctx)
            rb.text = wt
            rb.textSize = 16f
            radioGroup.addView(rb)
        }
        wasteTypeContainer.addView(radioGroup)
        radioGroup.setOnCheckedChangeListener { group, _ ->
            selectedWasteType = group.findViewById<RadioButton>(group.checkedRadioButtonId)?.text?.toString()
            schedule.isEnabled = !selectedWasteType.isNullOrEmpty()
        }
        btnPrev.setOnClickListener { selectedDate = selectedDate.minusDays(1); fmtDate() }
        btnNext.setOnClickListener { selectedDate = selectedDate.plusDays(1); fmtDate() }
        cancel.text = languageManager.getLocalizedStringForLanguage(lang, "cancel_button")
        schedule.text = languageManager.getLocalizedStringForLanguage(lang, "schedule_button")
        val dialog = AlertDialog.Builder(ctx).setView(dialogView).create()
        cancel.setOnClickListener { dialog.dismiss() }
        schedule.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }
}

private sealed class WasteRow { object Header: WasteRow(); data class Section(val titleKey: String): WasteRow(); data class Item(val data: com.example.wastemanagement.ui.screens.CollectionItem, val completed: Boolean): WasteRow() }

private class WasteCollectionAdapter(
    private val languageManager: LanguageManager,
    private var lang: AppLanguage
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val rows = mutableListOf<WasteRow>()
    fun rebuild(language: AppLanguage) {
        lang = language
        rows.clear()
        rows += WasteRow.Header
        rows += WasteRow.Section("upcoming_collections")
        com.example.wastemanagement.ui.screens.getSampleCollections().forEach { rows += WasteRow.Item(it, false) }
        rows += WasteRow.Section("recent_collections")
        com.example.wastemanagement.ui.screens.getSampleRecentCollections().forEach { rows += WasteRow.Item(it, true) }
        notifyDataSetChanged()
    }
    override fun getItemViewType(position: Int): Int = when(rows[position]) { is WasteRow.Header -> 0; is WasteRow.Section -> 1; is WasteRow.Item -> 2 }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when(viewType){
        0 -> HeaderVH(LayoutInflater.from(parent.context).inflate(R.layout.item_collection_header, parent, false))
        1 -> SectionVH(LayoutInflater.from(parent.context).inflate(R.layout.item_section_title, parent, false))
        else -> ItemVH(LayoutInflater.from(parent.context).inflate(R.layout.item_collection_card, parent, false))
    }
    override fun getItemCount(): Int = rows.size
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(val row = rows[position]) {
            is WasteRow.Header -> (holder as HeaderVH).bind(languageManager, lang)
            is WasteRow.Section -> (holder as SectionVH).bind(languageManager, lang, row.titleKey)
            is WasteRow.Item -> (holder as ItemVH).bind(row.data, row.completed)
        }
    }
    private class HeaderVH(v: View): RecyclerView.ViewHolder(v) {
        private val title: TextView = v.findViewById(R.id.textHeaderTitle)
        private val desc: TextView = v.findViewById(R.id.textHeaderDesc)
        fun bind(languageManager: LanguageManager, lang: AppLanguage) {
            title.text = languageManager.getLocalizedStringForLanguage(lang, "collection_overview_title")
            desc.text = languageManager.getLocalizedStringForLanguage(lang, "collection_overview_desc")
        }
    }
    private class SectionVH(v: View): RecyclerView.ViewHolder(v) {
        private val text: TextView = v as TextView
        fun bind(languageManager: LanguageManager, lang: AppLanguage, key: String) { text.text = languageManager.getLocalizedStringForLanguage(lang, key) }
    }
    private class ItemVH(v: View): RecyclerView.ViewHolder(v) {
        private val type: TextView = v.findViewById(R.id.textWasteType)
        private val date: TextView = v.findViewById(R.id.textDate)
        private val notes: TextView = v.findViewById(R.id.textNotes)
        private val icon: ImageView = v.findViewById(R.id.iconType)
        private val end: ImageView = v.findViewById(R.id.iconEnd)
        fun bind(item: com.example.wastemanagement.ui.screens.CollectionItem, completed: Boolean) {
            type.text = item.wasteType
            date.text = item.date
            if (item.notes.isNotEmpty()) { notes.visibility = View.VISIBLE; notes.text = item.notes } else notes.visibility = View.GONE
            val res = when(item.wasteType) {
                "General Waste" -> R.drawable.ic_delete
                "Recyclables" -> R.drawable.ic_autorenew
                "Organic" -> R.drawable.ic_eco
                "Hazardous" -> R.drawable.ic_warning
                "Electronics" -> R.drawable.ic_memory
                else -> R.drawable.ic_delete
            }
            icon.setImageResource(res)
            if (completed) {
                end.setImageResource(R.drawable.ic_check_circle)
            } else {
                end.setImageResource(R.drawable.ic_info)
            }
        }
    }
}

