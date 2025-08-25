package com.example.wastemanagement.data

import java.time.LocalDateTime

// Waste Collection Models
data class WasteCollection(
    val id: String,
    val wasteType: WasteType,
    val scheduledDate: LocalDateTime,
    val status: CollectionStatus,
    val notes: String = "",
    val location: String = "",
    val weight: Double? = null,
    val completedDate: LocalDateTime? = null
)

enum class WasteType(val displayName: String, val icon: String) {
    GENERAL_WASTE("General Waste", "🗑️"),
    RECYCLABLES("Recyclables", "♻️"),
    ORGANIC("Organic", "🌱"),
    HAZARDOUS("Hazardous", "⚠️"),
    ELECTRONICS("Electronics", "📱"),
    PAPER("Paper & Cardboard", "📄"),
    GLASS("Glass", "🥃"),
    METAL("Metal", "🥫")
}

enum class CollectionStatus(val displayName: String) {
    SCHEDULED("Scheduled"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled")
}

// User Profile Models
data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val phone: String? = null,
    val address: String? = null,
    val preferences: UserPreferences = UserPreferences()
)

data class UserPreferences(
    val notificationsEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false,
    val autoScheduleEnabled: Boolean = true,
    val preferredCollectionTime: String = "09:00",
    val language: String = "en"
)

// Recycling Guide Models
data class RecyclingCategory(
    val id: String,
    val name: String,
    val description: String,
    val items: List<String>,
    val tips: List<String>,
    val icon: String
)

data class RecyclingTip(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val icon: String
)

// Statistics Models
data class WasteStatistics(
    val totalCollections: Int = 0,
    val totalRecycled: Double = 0.0, // in kg
    val currentStreak: Int = 0,
    val monthlyStats: List<MonthlyStat> = emptyList()
)

data class MonthlyStat(
    val month: String,
    val collections: Int,
    val recycled: Double,
    val wasteType: WasteType
)

// Location Models
data class PickupLocation(
    val id: String,
    val name: String,
    val address: String,
    val coordinates: Coordinates? = null,
    val isDefault: Boolean = false
)

data class Coordinates(
    val latitude: Double,
    val longitude: Double
)

// Notification Models
data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val timestamp: LocalDateTime,
    val isRead: Boolean = false
)

enum class NotificationType {
    COLLECTION_REMINDER,
    COLLECTION_CONFIRMATION,
    RECYCLING_TIP,
    SYSTEM_UPDATE
}





