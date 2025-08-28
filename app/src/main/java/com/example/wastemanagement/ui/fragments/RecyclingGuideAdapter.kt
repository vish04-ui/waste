package com.example.wastemanagement.ui.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.wastemanagement.R
import com.example.wastemanagement.ui.localization.AppLanguage
import com.example.wastemanagement.ui.localization.LanguageManager

internal sealed class RecyclingGuideRow {
    data object Header: RecyclingGuideRow()
    data class Category(val data: RecyclingCategoryData): RecyclingGuideRow()
    data object QuickTipsTitle: RecyclingGuideRow()
    data class QuickTip(val text: String): RecyclingGuideRow()
}

internal data class RecyclingCategoryData(
    val id: String,
    val name: String,
    val description: String,
    val iconRes: Int,
    val items: List<String>,
    val tips: List<String>,
    var expanded: Boolean = false
)

class RecyclingGuideAdapter(
    private val languageManager: LanguageManager,
    private var language: AppLanguage
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val rows = mutableListOf<RecyclingGuideRow>()

    fun rebuild(language: AppLanguage) {
        this.language = language
        rows.clear()
        rows += RecyclingGuideRow.Header
        val cats = buildCategories(languageManager, language)
        rows += cats.map { RecyclingGuideRow.Category(it) }
        rows += RecyclingGuideRow.QuickTipsTitle
        rows += buildQuickTips(languageManager, language).map { RecyclingGuideRow.QuickTip(it) }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int = when(rows[position]) {
        is RecyclingGuideRow.Header -> 0
        is RecyclingGuideRow.Category -> 1
        is RecyclingGuideRow.QuickTipsTitle -> 2
        is RecyclingGuideRow.QuickTip -> 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inf = LayoutInflater.from(parent.context)
        return when(viewType) {
            0 -> HeaderVH(inf.inflate(R.layout.item_recycling_header, parent, false))
            1 -> CategoryVH(inf.inflate(R.layout.item_recycling_category, parent, false))
            2 -> QuickTipsTitleVH(inf.inflate(R.layout.item_quick_tips_title, parent, false))
            else -> QuickTipVH(inf.inflate(R.layout.item_quick_tip, parent, false))
        }
    }

    override fun getItemCount(): Int = rows.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(val row = rows[position]) {
            is RecyclingGuideRow.Header -> (holder as HeaderVH).bind(languageManager, language)
            is RecyclingGuideRow.Category -> (holder as CategoryVH).bind(row.data) {
                row.data.expanded = !row.data.expanded
                notifyItemChanged(position)
            }
            is RecyclingGuideRow.QuickTipsTitle -> (holder as QuickTipsTitleVH).bind(languageManager, language)
            is RecyclingGuideRow.QuickTip -> (holder as QuickTipVH).bind(row.text)
        }
    }

    private class HeaderVH(view: View): RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.textHeaderTitle)
        private val subtitle: TextView = view.findViewById(R.id.textHeaderSubtitle)
        fun bind(languageManager: LanguageManager, lang: AppLanguage) {
            title.text = languageManager.getLocalizedStringForLanguage(lang, "recycling_header_title")
            subtitle.text = languageManager.getLocalizedStringForLanguage(lang, "recycling_header_subtitle")
        }
    }

    private class CategoryVH(view: View): RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.textCategoryName)
        private val desc: TextView = view.findViewById(R.id.textCategoryDesc)
        private val icon: ImageView = view.findViewById(R.id.iconCategory)
        private val expand: ImageView = view.findViewById(R.id.iconExpand)
        private val expandedContent: LinearLayout = view.findViewById(R.id.expandedContent)
        private val headerRow: View = view.findViewById(R.id.headerRow)

        fun bind(data: RecyclingCategoryData, toggle: () -> Unit) {
            name.text = data.name
            desc.text = data.description
            icon.setImageResource(data.iconRes)
            if (data.expanded) {
                expandedContent.isVisible = true
                if (expandedContent.childCount == 0) {
                    val ctx = expandedContent.context
                    val label = TextView(ctx).apply {
                        text = ctx.getString(R.string.what_to_include)
                        textSize = 16f
                        setTextColor(name.currentTextColor)
                        setPadding(0,0,0,12)
                        setTypeface(typeface, android.graphics.Typeface.BOLD)
                    }
                    expandedContent.addView(label)
                    data.items.forEach { itemText ->
                        expandedContent.addView(buildRow(itemText, R.drawable.ic_check))
                    }
                    if (data.tips.isNotEmpty()) {
                        val tipsLabel = TextView(ctx).apply {
                            text = ctx.getString(R.string.tips_label)
                            textSize = 16f
                            setTextColor(name.currentTextColor)
                            setPadding(0,24,0,12)
                            setTypeface(typeface, android.graphics.Typeface.BOLD)
                        }
                        expandedContent.addView(tipsLabel)
                        data.tips.forEach { tipText ->
                            expandedContent.addView(buildRow(tipText, R.drawable.ic_info_secondary))
                        }
                    }
                }
            } else {
                expandedContent.isGone = true
                expandedContent.removeAllViews()
            }
            expand.rotation = if (data.expanded) 180f else 0f
            headerRow.setOnClickListener {
                toggle()
                expand.animate().rotation(if (data.expanded) 0f else 180f).setDuration(220).setInterpolator(AccelerateDecelerateInterpolator()).start()
            }
        }

        private fun buildRow(text: String, iconRes: Int): View {
            val ctx = expandedContent.context
            val row = LinearLayout(ctx)
            row.orientation = LinearLayout.HORIZONTAL
            row.setPadding(0,12,0,12)
            val iv = ImageView(ctx)
            val size = (18 * ctx.resources.displayMetrics.density).toInt()
            val lp = LinearLayout.LayoutParams(size,size)
            iv.layoutParams = lp
            iv.setImageResource(iconRes)
            iv.setColorFilter(icon.imageTintList?.defaultColor ?: name.currentTextColor)
            row.addView(iv)
            val tv = TextView(ctx)
            tv.text = text
            tv.textSize = 14f
            tv.setPadding(12,0,0,0)
            tv.setTextColor(name.currentTextColor)
            row.addView(tv)
            return row
        }
    }

    private class QuickTipsTitleVH(view: View): RecyclerView.ViewHolder(view) {
        private val tv: TextView = view as TextView
        fun bind(languageManager: LanguageManager, lang: AppLanguage) {
            tv.text = languageManager.getLocalizedStringForLanguage(lang, "quick_tips")
        }
    }

    private class QuickTipVH(view: View): RecyclerView.ViewHolder(view) {
        private val text: TextView = view.findViewById(R.id.textTip)
        fun bind(t: String) { text.text = t }
    }
}

private fun buildCategories(languageManager: LanguageManager, lang: AppLanguage): List<RecyclingCategoryData> {
    fun l(key: String) = languageManager.getLocalizedStringForLanguage(lang, key)
    return listOf(
        RecyclingCategoryData(
            id = "paper",
            name = l("paper_cardboard"),
            description = l("recyclable_paper_products"),
            iconRes = R.drawable.ic_description,
            items = listOf(l("item_newspapers_magazines"), l("item_cardboard_boxes"), l("item_office_paper"), l("item_junk_mail"), l("item_paper_bags")),
            tips = listOf(l("tip_remove_plastic_windows"), l("tip_flatten_cardboard"), l("tip_keep_paper_dry"))
        ),
        RecyclingCategoryData(
            id = "plastic",
            name = l("plastic"),
            description = l("plastic_desc"),
            iconRes = R.drawable.ic_science,
            items = listOf(l("item_water_bottles"), l("item_milk_jugs"), l("item_food_containers"), l("item_shampoo_bottles"), l("item_cleaning_product_bottles")),
            tips = listOf(l("tip_rinse_containers"), l("tip_check_numbers"), l("tip_remove_caps_labels"))
        ),
        RecyclingCategoryData(
            id = "glass",
            name = l("glass"),
            description = l("glass_desc"),
            iconRes = R.drawable.ic_wine_bar,
            items = listOf(l("item_beverage_bottles"), l("item_food_jars"), l("item_condiment_bottles"), l("item_cosmetic_containers")),
            tips = listOf(l("tip_rinse_thoroughly"), l("tip_remove_metal_lids"), l("tip_dont_break_glass"))
        ),
        RecyclingCategoryData(
            id = "metal",
            name = l("metal"),
            description = l("metal_desc"),
            iconRes = R.drawable.ic_build,
            items = listOf(l("item_aluminum_cans"), l("item_steel_food_cans"), l("item_aerosol_cans"), l("item_aluminum_foil")),
            tips = listOf(l("tip_rinse_food_residue"), l("tip_crush_cans"), l("tip_check_aerosol_empty"))
        )
    )
}

private fun buildQuickTips(languageManager: LanguageManager, lang: AppLanguage): List<String> {
    fun l(key: String) = languageManager.getLocalizedStringForLanguage(lang, key)
    return listOf(
        l("tip_rinse_containers"),
        l("tip_check_guidelines"),
        l("tip_when_in_doubt"),
        l("tip_recycle_batteries")
    )
}
