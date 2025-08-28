package com.example.wastemanagement.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wastemanagement.R
import kotlinx.coroutines.launch

class WasteTrackingFragment: Fragment() {
    private val entries = mutableListOf<WasteEntry>()
    private lateinit var adapter: WasteTrackingAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_waste_tracking, container, false)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val list = view.findViewById<RecyclerView>(R.id.recyclerEntries)
        adapter = WasteTrackingAdapter(entries)
        list.layoutManager = LinearLayoutManager(requireContext())
        list.adapter = adapter
        val weight = view.findViewById<EditText>(R.id.inputWeight)
        val notes = view.findViewById<EditText>(R.id.inputNotes)
        view.findViewById<Button>(R.id.buttonAdd).setOnClickListener {
            val w = weight.text.toString().toDoubleOrNull() ?: return@setOnClickListener
            lifecycleScope.launch {
                entries.add(0, WasteEntry(w, notes.text.toString()))
                adapter.notifyItemInserted(0)
                list.scrollToPosition(0)
                weight.text.clear(); notes.text.clear()
            }
        }
    }
}

data class WasteEntry(val weight: Double, val notes: String)

private class WasteTrackingAdapter(private val items: List<WasteEntry>): RecyclerView.Adapter<WasteTrackingVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WasteTrackingVH = WasteTrackingVH(LayoutInflater.from(parent.context).inflate(R.layout.item_waste_entry, parent, false))
    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: WasteTrackingVH, position: Int) = holder.bind(items[position])
}

private class WasteTrackingVH(v: View): RecyclerView.ViewHolder(v) {
    private val weight: android.widget.TextView = v.findViewById(R.id.textWeight)
    private val notes: android.widget.TextView = v.findViewById(R.id.textNotes)
    fun bind(entry: WasteEntry){
        weight.text = "${entry.weight} kg"
        if(entry.notes.isBlank()) notes.visibility = View.GONE else { notes.visibility = View.VISIBLE; notes.text = entry.notes }
    }
}
