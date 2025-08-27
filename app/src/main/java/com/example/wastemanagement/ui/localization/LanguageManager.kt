package com.example.wastemanagement.ui.localization

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale

private val Context.languageDataStore: DataStore<Preferences> by preferencesDataStore(name = "language_preferences")

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    SINHALA("si", "Sinhala", "සිංහල"),
    TAMIL("ta", "Tamil", "தமிழ்");
    
    companion object {
        fun fromCode(code: String): AppLanguage {
            return values().find { it.code == code } ?: ENGLISH
        }
    }
}

class LanguageManager(private val context: Context) {
    
    companion object {
        private val SELECTED_LANGUAGE = stringPreferencesKey("selected_language")
    }
    
    val currentLanguage: Flow<AppLanguage> = context.languageDataStore.data
        .map { preferences ->
            val languageCode = preferences[SELECTED_LANGUAGE] ?: AppLanguage.ENGLISH.code
            AppLanguage.fromCode(languageCode)
        }
    
    suspend fun setLanguage(language: AppLanguage) {
        context.languageDataStore.edit { preferences ->
            preferences[SELECTED_LANGUAGE] = language.code
        }
        applyLanguage(language)
    }
    
    fun applyLanguage(language: AppLanguage) {
        val desired = LocaleListCompat.forLanguageTags(language.code)
        val current = AppCompatDelegate.getApplicationLocales()
        if (current.toLanguageTags() != desired.toLanguageTags()) {
            AppCompatDelegate.setApplicationLocales(desired)
        }
    }
    
    // Get localized string based on current language
    fun getLocalizedString(key: String): String {
        return getEnglishString(key) // Default fallback
    }
    
    // Get localized string for a specific language
    fun getLocalizedStringForLanguage(language: AppLanguage, key: String): String {
        return when (language) {
            AppLanguage.SINHALA -> getSinhalaString(key)
            AppLanguage.TAMIL -> getTamilString(key)
            else -> getEnglishString(key)
        }
    }
    
    private fun getEnglishString(key: String): String {
        return when (key) {
            "dashboard" -> "Dashboard"
            "collection" -> "Collection"
            "recycling" -> "Recycling"
            "profile" -> "Profile"
            "settings" -> "Settings"
            // Auth & Onboarding
            "get_started" -> "Get Started"
            "next" -> "Next"
            "skip" -> "Skip"
            "finish" -> "Finish"
            "onboarding_title_1" -> "Track Your Waste"
            "onboarding_desc_1" -> "Log and monitor your waste collections with ease."
            "onboarding_title_2" -> "Recycle Smarter"
            "onboarding_desc_2" -> "Learn best practices to recycle properly."
            "onboarding_title_3" -> "Make an Impact"
            "onboarding_desc_3" -> "Join EcoGrid to keep your community cleaner."
            "email" -> "Email"
            "password" -> "Password"
            "name" -> "Name"
            "login" -> "Login"
            "signup" -> "Sign Up"
            "dont_have_account" -> "Don't have an account?"
            "have_account" -> "Already have an account?"
            "continue" -> "Continue"
            "welcome_title" -> "Welcome to EcoGrid"
            "welcome_subtitle" -> "Let's make the world cleaner together"
            "collections" -> "Collections"
            "recycled" -> "Recycled"
            "streak" -> "Streak"
            "waste_collection" -> "Waste Collection"
            "waste_collection_desc" -> "Schedule pickups and track collections"
            "waste_collection_title" -> "Waste Collection"
            "collection_overview_title" -> "Collection Overview"
            "collection_overview_desc" -> "Manage your waste collection schedule and track progress"
            "upcoming_collections" -> "Upcoming Collections"
            "recent_collections" -> "Recent Collections"
            "schedule_pickup" -> "Schedule Pickup"
            "schedule_collection_title" -> "Schedule Collection"
            "select_date" -> "Select Date:"
            "waste_type" -> "Waste Type:"
            "waste_type_general" -> "General Waste"
            "waste_type_recyclables" -> "Recyclables"
            "waste_type_organic" -> "Organic"
            "waste_type_hazardous" -> "Hazardous"
            "waste_type_electronics" -> "Electronics"
            "schedule_button" -> "Schedule"
            "cancel_button" -> "Cancel"
            "recycling_guide" -> "Recycling Guide"
            "recycling_guide_title" -> "Recycling Guide"
            "recycling_header_title" -> "Learn How to Recycle Properly"
            "recycling_header_subtitle" -> "Reduce waste and help the environment"
            "quick_tips" -> "Quick Tips"
            "what_to_include" -> "What to include:"
            "tips_label" -> "Tips:"
            "paper_cardboard" -> "Paper & Cardboard"
            "recyclable_paper_products" -> "Recyclable paper products"
            "plastic" -> "Plastic"
            "plastic_desc" -> "Plastic containers and packaging"
            "glass" -> "Glass"
            "glass_desc" -> "Glass bottles and jars"
            "metal" -> "Metal"
            "metal_desc" -> "Aluminum and steel containers"
            "item_newspapers_magazines" -> "Newspapers and magazines"
            "item_cardboard_boxes" -> "Cardboard boxes"
            "item_office_paper" -> "Office paper"
            "item_junk_mail" -> "Junk mail"
            "item_paper_bags" -> "Paper bags"
            "tip_remove_plastic_windows" -> "Remove plastic windows from envelopes"
            "tip_flatten_cardboard" -> "Flatten cardboard boxes"
            "tip_keep_paper_dry" -> "Keep paper dry and clean"
            "item_water_bottles" -> "Water bottles"
            "item_milk_jugs" -> "Milk jugs"
            "item_food_containers" -> "Food containers"
            "item_shampoo_bottles" -> "Shampoo bottles"
            "item_cleaning_product_bottles" -> "Cleaning product bottles"
            "tip_check_numbers" -> "Check recycling numbers (1-7)"
            "tip_remove_caps_labels" -> "Remove caps and labels when possible"
            "item_beverage_bottles" -> "Beverage bottles"
            "item_food_jars" -> "Food jars"
            "item_condiment_bottles" -> "Condiment bottles"
            "item_cosmetic_containers" -> "Cosmetic containers"
            "tip_rinse_thoroughly" -> "Rinse thoroughly"
            "tip_remove_metal_lids" -> "Remove metal lids"
            "tip_dont_break_glass" -> "Don't break glass - it's dangerous"
            "item_aluminum_cans" -> "Aluminum cans"
            "item_steel_food_cans" -> "Steel food cans"
            "item_aerosol_cans" -> "Aerosol cans (empty)"
            "item_aluminum_foil" -> "Aluminum foil (clean)"
            "tip_rinse_food_residue" -> "Rinse food residue"
            "tip_crush_cans" -> "Crush aluminum cans to save space"
            "tip_check_aerosol_empty" -> "Check if aerosol cans are completely empty"
            "tip_rinse_containers" -> "Always rinse containers before recycling"
            "tip_check_guidelines" -> "Check local recycling guidelines"
            "tip_when_in_doubt" -> "When in doubt, throw it out"
            "tip_recycle_batteries" -> "Recycle batteries at designated locations"
            "recycling_guide_desc" -> "Learn how to recycle properly"
            "profile_desc" -> "Manage your account and preferences"
            "history" -> "History"
            "history_desc" -> "View past collections and activities"
            "dark_mode" -> "Dark Mode"
            "dark_mode_desc" -> "Use dark theme for the app"
            "auto_schedule" -> "Auto Schedule"
            "auto_schedule_desc" -> "Automatically schedule regular pickups"
            "notifications" -> "Notifications"
            "notifications_desc" -> "Receive alerts and updates"
            "account" -> "Account"
            "edit_profile" -> "Edit Profile"
            "edit_profile_desc" -> "Update your personal information"
            "address_settings" -> "Address Settings"
            "address_settings_desc" -> "Manage pickup locations"
            "help_support" -> "Help & Support"
            "help_support_desc" -> "Get help and contact support"
            "about" -> "About"
            "about_desc" -> "App version and information"
            "logout" -> "Logout"
            "language" -> "Language"
            "language_desc" -> "Choose your preferred language"
            "select_language" -> "Select Language"
            else -> key
        }
    }
    
    private fun getSinhalaString(key: String): String {
        return when (key) {
            "dashboard" -> "Dashboard"
            "collection" -> "Collection"
            "recycling" -> "Recycling"
            "profile" -> "Profile"
            "settings" -> "அமைப்புகள்"
            // Auth & Onboarding (fallbacks/mixed localized)
            "get_started" -> "ආරම්භ කරන්න"
            "next" -> "ඊළඟ"
            "skip" -> "මඟ හරින්න"
            "finish" -> "අවසන්"
            "onboarding_title_1" -> "ඔබගේ අපද්‍රව්ය අධීක්ෂණය කරන්න"
            "onboarding_desc_1" -> "එළෙසම සටහන් තබා ගන්න."
            "onboarding_title_2" -> "සුදානම්ව නැවුම් පරිවර්තනය"
            "onboarding_desc_2" -> "නිවැරදිව නැවත පාවිච්චි කරන්නේ කෙසේද කියා ඉගෙන ගන්න."
            "onboarding_title_3" -> "ආසන්න පරිසරයට බලපෑමක් කරන්න"
            "onboarding_desc_3" -> "EcoGrid එක්වෙමින් ඔබේ ප්‍රජාව පිරිසිදු කරමු."
            "email" -> "Email"
            "password" -> "Password"
            "name" -> "නම"
            "login" -> "Login"
            "signup" -> "Sign Up"
            "dont_have_account" -> "ගිණුමක් නැද්ද?"
            "have_account" -> "දැනටමත් ගිණුමක් තිබේද?"
            "continue" -> "Continue"
            "welcome_title" -> "Welcome to EcoGrid"
            "welcome_subtitle" -> "ලෝකය පිරිසිදු කරමු"
            "collections" -> "Collections"
            "recycled" -> "Recycled"
            "streak" -> "Streak"
            "waste_collection" -> "Waste Collection"
            "waste_collection_desc" -> "Pickup scheduling සහ tracking"
            "waste_collection_title" -> "Waste Collection"
            "collection_overview_title" -> "Collection Overview"
            "collection_overview_desc" -> "ඔබගේ waste collection schedule පාලනය කර ප්‍රගතිය නිරීක්ෂණය කරන්න"
            "upcoming_collections" -> "Upcoming Collections"
            "recent_collections" -> "Recent Collections"
            "schedule_pickup" -> "Schedule Pickup"
            "schedule_collection_title" -> "Schedule Collection"
            "select_date" -> "දිනය තෝරාගන්න:"
            "waste_type" -> "Waste Type:"
            "waste_type_general" -> "General Waste"
            "waste_type_recyclables" -> "Recyclables"
            "waste_type_organic" -> "Organic"
            "waste_type_hazardous" -> "Hazardous"
            "waste_type_electronics" -> "Electronics"
            "schedule_button" -> "Schedule"
            "cancel_button" -> "Cancel"
            "recycling_guide" -> "Recycling Guide"
            "recycling_guide_title" -> "Recycling Guide"
            "recycling_header_title" -> "Recycling නිවැරදිව කරන බව ඉගෙන ගන්න"
            "recycling_header_subtitle" -> "අපද්‍රව්ය අඩු කර පරිසරයට උදව් කරන්න"
            "quick_tips" -> "Quick Tips"
            "what_to_include" -> "சேர்க்க வேண்டியது:"
            "tips_label" -> "குறிப்புகள்:"
            "paper_cardboard" -> "Paper & Cardboard"
            "recyclable_paper_products" -> "மறுசுழற்சி செய்யக்கூடிய காகிதப் பொருட்கள்"
            "plastic" -> "Plastic"
            "plastic_desc" -> "பிளாஸ்டிக் பாத்திரங்கள் மற்றும் பொதிகள்"
            "glass" -> "Glass"
            "glass_desc" -> "கண்ணாடி பாட்டில்கள் மற்றும் ஜாடிகள்"
            "metal" -> "Metal"
            "metal_desc" -> "அலுமினியம் மற்றும் எஃகு பாத்திரங்கள்"
            "item_newspapers_magazines" -> "செய்தித்தாள்கள் மற்றும் இதழ்கள்"
            "item_cardboard_boxes" -> "கார்ட்போர்டு பெட்டிகள்"
            "item_office_paper" -> "அலுவலக காகிதம்"
            "item_junk_mail" -> "அவசியமற்ற அஞ்சல்"
            "item_paper_bags" -> "காகித பைகள்"
            "tip_remove_plastic_windows" -> "அஞ்சல் உறைகளின் பிளாஸ்டிக் ஜன்னல்கள் அகற்றவும்"
            "tip_flatten_cardboard" -> "கார்ட்போர்டு பெட்டிகளை தட்டையாக்கவும்"
            "tip_keep_paper_dry" -> "காகிதத்தை வறண்டதும் சுத்தமாகவும் வைத்திருங்கள்"
            "item_water_bottles" -> "தண்ணீர் பாட்டில்கள்"
            "item_milk_jugs" -> "பால் குடுவைகள்"
            "item_food_containers" -> "உணவு பாத்திரங்கள்"
            "item_shampoo_bottles" -> "ஷாம்பு பாட்டில்கள்"
            "item_cleaning_product_bottles" -> "சுத்தம் செய்யும் தயாரிப்பு பாட்டில்கள்"
            "tip_check_numbers" -> "மறுசுழற்சி எண்கள் (1–7) சரிபார்க்கவும்"
            "tip_remove_caps_labels" -> "முடிகளை மற்றும் லேபிள்களை இயன்றால் அகற்றவும்"
            "item_beverage_bottles" -> "பான பாட்டில்கள்"
            "item_food_jars" -> "உணவு ஜாடிகள்"
            "item_condiment_bottles" -> "கண்டிமென்ட் பாட்டில்கள்"
            "item_cosmetic_containers" -> "அலங்கார பாத்திரங்கள்"
            "tip_rinse_thoroughly" -> "நன்றாக கழுவவும்"
            "tip_remove_metal_lids" -> "உலோக மூடிகளை அகற்றவும்"
            "tip_dont_break_glass" -> "கண்ணாடியை உடைக்க வேண்டாம் — அபாயகரம்"
            "item_aluminum_cans" -> "அலுமினியம் டின்கள்"
            "item_steel_food_cans" -> "எஃகு உணவு டின்கள்"
            "item_aerosol_cans" -> "ஏரோசல் டின்கள் (காலி)"
            "item_aluminum_foil" -> "அலுமினியம் ஃபாயில் (சுத்தம்)"
            "tip_rinse_food_residue" -> "உணவு மீதிகளை கழுவி அகற்றவும்"
            "tip_crush_cans" -> "இடத்தைச் சேமிக்க டின்களை நசுக்கவும்"
            "tip_check_aerosol_empty" -> "ஏரோசல் டின் முழுவதும் காலியா என்பதை சரிபார்க்கவும்"
            "tip_rinse_containers" -> "පිරිසිදු කර නැවත පාවිච්චි කිරීමට පෙර සැරසිල්ල rinse කරන්න"
            "tip_check_guidelines" -> "ස්ථානික recycling మార్గෝපදේශ පරීක්ෂා කරන්න"
            "tip_when_in_doubt" -> "සැකයි නම්, එය ඉවත දමන්න"
            "tip_recycle_batteries" -> "බැටරි නියමිත ස්ථානවල නැවත භාවිතයට දෙන්න"
            "recycling_guide_desc" -> "Recycling කරන්නේ කෙසේද යන්න ඉගෙන ගන්න"
            "profile_desc" -> "Manage your account සහ preferences"
            "history" -> "History"
            "history_desc" -> "Past collections සහ activities බලන්න"
            "dark_mode" -> "Dark Mode"
            "dark_mode_desc" -> "App සඳහා dark theme භාවිතා කරන්න"
            "auto_schedule" -> "Auto Schedule"
            "auto_schedule_desc" -> "Regular pickups automatically schedule කරන්න"
            "notifications" -> "Notifications"
            "notifications_desc" -> "Alerts සහ updates ලබා ගන්න"
            "account" -> "Account"
            "edit_profile" -> "Edit Profile"
            "edit_profile_desc" -> "Update your personal information"
            "address_settings" -> "Address Settings"
            "address_settings_desc" -> "Pickup locations manage කරන්න"
            "help_support" -> "Help & Support"
            "help_support_desc" -> "Help ලබා ගන්න සහ support contact කරන්න"
            "about" -> "About"
            "about_desc" -> "App version සහ information"
            "logout" -> "Logout"
            "language" -> "Language"
            "language_desc" -> "Choose your preferred language"
            "select_language" -> "භාෂාව තෝරන්න"
            else -> key
        }
    }
    
    private fun getTamilString(key: String): String {
        return when (key) {
            "dashboard" -> "Dashboard"
            "collection" -> "Collection"
            "recycling" -> "Recycling"
            "profile" -> "Profile"
            "settings" -> "Settings"
            // Auth & Onboarding
            "get_started" -> "தொடங்கவும்"
            "next" -> "அடுத்து"
            "skip" -> "தவிர்க்கவும்"
            "finish" -> "முடிக்க"
            "onboarding_title_1" -> "உங்கள் கழிவை கண்காணிக்கவும்"
            "onboarding_desc_1" -> "சுலபமாக சேகரிப்புகளை பதிவு செய்யுங்கள்."
            "onboarding_title_2" -> "சிறந்த முறையில் மறுசுழற்சி"
            "onboarding_desc_2" -> "சரியாக மறுசுழற்சி செய்வதை கற்றுக்கொள்ளுங்கள்."
            "onboarding_title_3" -> "ஒரு நல்ல தாக்கம் உண்டாக்குங்கள்"
            "onboarding_desc_3" -> "EcoGrid-இல் சேர்ந்து உங்கள் சமூகத்தை சுத்தமாக வைத்திருக்கவும்."
            "email" -> "Email"
            "password" -> "Password"
            "name" -> "பெயர்"
            "login" -> "Login"
            "signup" -> "Sign Up"
            "dont_have_account" -> "கணக்கு இல்லையா?"
            "have_account" -> "ஏற்கனவே கணக்கு உள்ளதா?"
            "continue" -> "Continue"
            "welcome_title" -> "Welcome to EcoGrid"
            "welcome_subtitle" -> "உலகத்தை சுத்தமாக்குவோம்"
            "collections" -> "Collections"
            "recycled" -> "Recycled"
            "streak" -> "Streak"
            "waste_collection" -> "Waste Collection"
            "waste_collection_desc" -> "Pickup scheduling மற்றும் tracking"
            "waste_collection_title" -> "Waste Collection"
            "collection_overview_title" -> "Collection Overview"
            "collection_overview_desc" -> "உங்கள் கழிவு சேகரிப்பு அட்டவணையை நிர்வகித்து முன்னேற்றத்தை கண்காணிக்கவும்"
            "upcoming_collections" -> "Upcoming Collections"
            "recent_collections" -> "Recent Collections"
            "schedule_pickup" -> "Schedule Pickup"
            "schedule_collection_title" -> "Schedule Collection"
            "select_date" -> "தேதியைத் தேர்ந்தெடுக்கவும்:"
            "waste_type" -> "Waste Type:"
            "waste_type_general" -> "General Waste"
            "waste_type_recyclables" -> "Recyclables"
            "waste_type_organic" -> "Organic"
            "waste_type_hazardous" -> "Hazardous"
            "waste_type_electronics" -> "Electronics"
            "schedule_button" -> "Schedule"
            "cancel_button" -> "Cancel"
            "recycling_guide" -> "Recycling Guide"
            "recycling_guide_title" -> "Recycling Guide"
            "recycling_header_title" -> "சரியாக மறுசுழற்சி செய்வதை கற்றுக்கொள்ளுங்கள்"
            "recycling_header_subtitle" -> "கழிவுகளை குறைத்து சூழலுக்கு உதவுங்கள்"
            "quick_tips" -> "Quick Tips"
            "what_to_include" -> "What to include:"
            "tips_label" -> "Tips:"
            "paper_cardboard" -> "Paper & Cardboard"
            "recyclable_paper_products" -> "Recyclable paper products"
            "plastic" -> "Plastic"
            "plastic_desc" -> "Plastic containers and packaging"
            "glass" -> "Glass"
            "glass_desc" -> "Glass bottles and jars"
            "metal" -> "Metal"
            "metal_desc" -> "Aluminum and steel containers"
            "item_newspapers_magazines" -> "Newspapers and magazines"
            "item_cardboard_boxes" -> "Cardboard boxes"
            "item_office_paper" -> "Office paper"
            "item_junk_mail" -> "Junk mail"
            "item_paper_bags" -> "Paper bags"
            "tip_remove_plastic_windows" -> "Remove plastic windows from envelopes"
            "tip_flatten_cardboard" -> "Flatten cardboard boxes"
            "tip_keep_paper_dry" -> "Keep paper dry and clean"
            "item_water_bottles" -> "Water bottles"
            "item_milk_jugs" -> "Milk jugs"
            "item_food_containers" -> "Food containers"
            "item_shampoo_bottles" -> "Shampoo bottles"
            "item_cleaning_product_bottles" -> "Cleaning product bottles"
            "tip_check_numbers" -> "Check recycling numbers (1-7)"
            "tip_remove_caps_labels" -> "Remove caps and labels when possible"
            "item_beverage_bottles" -> "Beverage bottles"
            "item_food_jars" -> "Food jars"
            "item_condiment_bottles" -> "Condiment bottles"
            "item_cosmetic_containers" -> "Cosmetic containers"
            "tip_rinse_thoroughly" -> "Rinse thoroughly"
            "tip_remove_metal_lids" -> "Remove metal lids"
            "tip_dont_break_glass" -> "Don't break glass - it's dangerous"
            "item_aluminum_cans" -> "Aluminum cans"
            "item_steel_food_cans" -> "Steel food cans"
            "item_aerosol_cans" -> "Aerosol cans (empty)"
            "item_aluminum_foil" -> "Aluminum foil (clean)"
            "tip_rinse_food_residue" -> "Rinse food residue"
            "tip_crush_cans" -> "Crush aluminum cans to save space"
            "tip_check_aerosol_empty" -> "Check if aerosol cans are completely empty"
            "tip_rinse_containers" -> "மறுசுழற்சிக்கு முன் கொள்கலன்களை எப்போதும் கழுவவும்"
            "tip_check_guidelines" -> "உள்ளூர் மறுசுழற்சி வழிகாட்டுதல்களைச் சரிபார்க்கவும்"
            "tip_when_in_doubt" -> "சந்தேகம் இருந்தால், அதை வெளியேற்றுங்கள்"
            "tip_recycle_batteries" -> "பேட்டரிகளை நியமிக்கப்பட்ட இடங்களில் மறுசுழற்சி செய்யவும்"
            "recycling_guide_desc" -> "Recycling எப்படி செய்வது என்பதைக் கற்றுக்கொள்ளுங்கள்"
            "profile_desc" -> "உங்கள் account மற்றும் preferences ஐ manage செய்யுங்கள்"
            "history" -> "History"
            "history_desc" -> "Past collections மற்றும் activities ஐப் பாருங்கள்"
            "dark_mode" -> "Dark Mode"
            "dark_mode_desc" -> "App க்கு dark theme பயன்படுத்துங்கள்"
            "auto_schedule" -> "Auto Schedule"
            "auto_schedule_desc" -> "Regular pickups ஐ automatically schedule செய்யுங்கள்"
            "notifications" -> "Notifications"
            "notifications_desc" -> "Alerts மற்றும் updates ஐப் பெறுங்கள்"
            "account" -> "Account"
            "edit_profile" -> "Edit Profile"
            "edit_profile_desc" -> "Update your personal information"
            "address_settings" -> "Address Settings"
            "address_settings_desc" -> "Pickup locations ஐ manage செய்யுங்கள்"
            "help_support" -> "Help & Support"
            "help_support_desc" -> "Help பெறுங்கள் மற்றும் support contact செய்யுங்கள்"
            "about" -> "About"
            "about_desc" -> "App version மற்றும் information"
            "logout" -> "Logout"
            "language" -> "Language"
            "language_desc" -> "Choose your preferred language"
            "select_language" -> "மொழியைத் தேர்ந்தெடுக்கவும்"
            else -> key
        }
    }
}
