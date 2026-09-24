package com.example.data.models

/**
 * Standard Django REST Framework Response Envelope
 */
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null,
    val errors: Map<String, List<String>>? = null
)

/**
 * UI State container for asynchronous backend calls
 */
sealed class ResourceState<out T> {
    object Loading : ResourceState<Nothing>()
    data class Success<out T>(val data: T) : ResourceState<T>()
    data class Empty(val message: String = "No data available") : ResourceState<Nothing>()
    data class Error(val message: String) : ResourceState<Nothing>()
}

/**
 * Gym Owner User Profile
 */
data class GymOwnerUser(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: String = "Owner",
    val profileImage: String? = null
)

/**
 * Gym Details & Location Model
 */
data class GymProfile(
    val id: String,
    val name: String,
    val description: String,
    val phone: String,
    val email: String,
    val website: String,
    val address: String,
    val city: String,
    val district: String,
    val state: String,
    val pincode: String,
    val openingTime: String,
    val closingTime: String,
    val latitude: Double,
    val longitude: Double,
    val logoRes: Int? = null
)

/**
 * Dashboard KPIs & Summary
 */
data class DashboardKpis(
    val totalCustomers: Int,
    val totalCustomersGrowth: Double,
    val activeMemberships: Int,
    val activeMembershipsGrowth: Double,
    val monthlyRevenue: Double,
    val monthlyRevenueGrowth: Double,
    val upcomingBookings: Int
)

data class RevenueDataPoint(
    val label: String,
    val amount: Double
)

data class MembershipOverview(
    val active: Int,
    val pending: Int,
    val expired: Int,
    val cancelled: Int
)

/**
 * Customer / Member Entity
 */
data class CustomerItem(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val membershipPlan: String,
    val startDate: String,
    val endDate: String,
    val status: String, // Active, Expired, Pending
    val joinedDate: String,
    val avatarInitials: String = ""
)

/**
 * Membership Entity
 */
data class MembershipItem(
    val id: String,
    val customerName: String,
    val customerEmail: String,
    val customerPhone: String = "",
    val planName: String,
    val gymName: String,
    val startDate: String,
    val endDate: String,
    val amount: Double,
    val status: String // Active, Pending, Expired, Cancelled
)

/**
 * Membership Plan Tier
 */
data class MembershipPlanItem(
    val id: String,
    val name: String,
    val description: String,
    val duration: Int,
    val durationUnit: String, // Months, Days, Year
    val price: Double,
    val discountPrice: Double? = null,
    val features: List<String>,
    val isActive: Boolean = true
)

/**
 * Payment Transaction Record
 */
data class PaymentItem(
    val transactionId: String,
    val customerName: String,
    val planName: String,
    val amount: Double,
    val provider: String, // Razorpay, Stripe, UPI, Cash
    val status: String, // Successful, Pending, Failed
    val date: String,
    val currency: String = "INR",
    val orderId: String = "",
    val paymentId: String = "",
    val paidAt: String = ""
)

/**
 * Booking Record
 */
data class BookingItem(
    val id: String,
    val customerName: String,
    val customerPhone: String = "",
    val membershipPlan: String,
    val date: String,
    val time: String,
    val status: String // Upcoming, Today, Completed, Cancelled
)

/**
 * Gym Staff Member
 */
data class StaffMember(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: String, // Gym Admin, Staff
    val status: String, // Active, Inactive
    val createdDate: String
)

/**
 * Notification Item
 */
data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val type: String, // Payment, Membership, Booking, System
    val timestamp: String,
    val isRead: Boolean
)
