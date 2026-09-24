package com.example.data.services

import com.example.R
import com.example.data.models.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Clean API Service & Repository Layer prepared for Django REST Framework + JWT
 * Base Endpoint Contract: /api/v1/
 */
object GymOwnerRepository {

    // Mock Gym Owner profile
    private var currentOwner = GymOwnerUser(
        id = "own_01",
        name = "Kishore Kumar",
        email = "kishore@ironhousefitness.com",
        phone = "+91 98470 12345",
        role = "Gym Owner",
        profileImage = null
    )

    // Mock Gym details (Iron House Fitness, Kozhikode, Kerala)
    private var currentGym = GymProfile(
        id = "gym_iron_house",
        name = "Iron House Fitness",
        description = "Premium high-performance training facility with modern strength and cardio zones.",
        phone = "+91 495 276 5432",
        email = "contact@ironhousefitness.com",
        website = "www.ironhousefitness.in",
        address = "4th Floor, Emerald Plaza, Mavoor Road",
        city = "Kozhikode",
        district = "Kozhikode",
        state = "Kerala",
        pincode = "673004",
        openingTime = "05:30 AM",
        closingTime = "10:30 PM",
        latitude = 11.2588,
        longitude = 75.7804,
        logoRes = R.drawable.byce_logo
    )

    private val samplePlans = mutableListOf(
        MembershipPlanItem(
            id = "plan_1",
            name = "Monthly",
            description = "Standard 1-month full gym floor access with locker facility",
            duration = 1,
            durationUnit = "Month",
            price = 1499.0,
            discountPrice = null,
            features = listOf("Full gym floor access", "Locker access", "App check-in pass", "Shower facility"),
            isActive = true
        ),
        MembershipPlanItem(
            id = "plan_2",
            name = "Quarterly",
            description = "3-month progressive training pass with complimentary fitness assessment",
            duration = 3,
            durationUnit = "Months",
            price = 3999.0,
            discountPrice = 3799.0,
            features = listOf("All Monthly features", "1 Fitness assessment", "Diet guidance sheet", "Guest pass (1x)"),
            isActive = true
        ),
        MembershipPlanItem(
            id = "plan_3",
            name = "Half Yearly",
            description = "6-month dedicated fitness membership with free steam and recovery sessions",
            duration = 6,
            durationUnit = "Months",
            price = 6999.0,
            discountPrice = 6499.0,
            features = listOf("All Quarterly features", "Steam bath access", "2 Body composition tests", "2 Guest passes"),
            isActive = true
        ),
        MembershipPlanItem(
            id = "plan_4",
            name = "Yearly",
            description = "Ultimate 12-month transformation membership with maximum savings",
            duration = 12,
            durationUnit = "Months",
            price = 11999.0,
            discountPrice = 10999.0,
            features = listOf("All Half Yearly features", "Priority slot booking", "Free BYCE gym kit", "Unlimited guest passes (1/mo)"),
            isActive = true
        )
    )

    private val sampleCustomers = mutableListOf(
        CustomerItem("c1", "Rahul Menon", "rahul.menon@gmail.com", "+91 98471 22334", "Yearly", "10 Jan 2026", "09 Jan 2027", "Active", "10 Jan 2025", "RM"),
        CustomerItem("c2", "Ananya Nair", "ananya.nair@outlook.com", "+91 97455 33445", "Monthly", "01 Sep 2026", "30 Sep 2026", "Active", "15 Jun 2026", "AN"),
        CustomerItem("c3", "Mohammed Faizal", "faizal.m@gmail.com", "+91 99951 44556", "Half Yearly", "15 Jul 2026", "14 Jan 2027", "Active", "15 Jul 2026", "MF"),
        CustomerItem("c4", "Deepak Varma", "deepak.v@yahoo.com", "+91 94471 55667", "Quarterly", "12 Aug 2026", "11 Nov 2026", "Active", "12 Feb 2026", "DV"),
        CustomerItem("c5", "Sneha Joseph", "sneha.j@gmail.com", "+91 96331 66778", "Monthly", "18 Aug 2026", "17 Sep 2026", "Expired", "18 May 2026", "SJ"),
        CustomerItem("c6", "Arjun Prasad", "arjun.p@gmail.com", "+91 98951 77889", "Yearly", "05 Sep 2026", "04 Sep 2027", "Pending", "05 Sep 2026", "AP"),
        CustomerItem("c7", "Pooja Krishnan", "pooja.k@gmail.com", "+91 97461 88990", "Quarterly", "20 Jun 2026", "19 Sep 2026", "Expired", "20 Dec 2025", "PK"),
        CustomerItem("c8", "Vivek Raj", "vivek.raj@gmail.com", "+91 95671 99001", "Half Yearly", "01 Aug 2026", "31 Jan 2027", "Active", "01 Aug 2026", "VR")
    )

    private val sampleMemberships = mutableListOf(
        MembershipItem("m1", "Rahul Menon", "rahul.menon@gmail.com", "+91 98471 22334", "Yearly", "Iron House Fitness", "10 Jan 2026", "09 Jan 2027", 11999.0, "Active"),
        MembershipItem("m2", "Ananya Nair", "ananya.nair@outlook.com", "+91 97455 33445", "Monthly", "Iron House Fitness", "01 Sep 2026", "30 Sep 2026", 1499.0, "Active"),
        MembershipItem("m3", "Mohammed Faizal", "faizal.m@gmail.com", "+91 99951 44556", "Half Yearly", "Iron House Fitness", "15 Jul 2026", "14 Jan 2027", 6999.0, "Active"),
        MembershipItem("m4", "Deepak Varma", "deepak.v@yahoo.com", "+91 94471 55667", "Quarterly", "Iron House Fitness", "12 Aug 2026", "11 Nov 2026", 3999.0, "Active"),
        MembershipItem("m5", "Sneha Joseph", "sneha.j@gmail.com", "+91 96331 66778", "Monthly", "Iron House Fitness", "18 Aug 2026", "17 Sep 2026", 1499.0, "Expired"),
        MembershipItem("m6", "Arjun Prasad", "arjun.p@gmail.com", "+91 98951 77889", "Yearly", "Iron House Fitness", "05 Sep 2026", "04 Sep 2027", 11999.0, "Pending"),
        MembershipItem("m7", "Rohan Pillai", "rohan.p@gmail.com", "+91 94470 11223", "Monthly", "Iron House Fitness", "10 Aug 2026", "09 Sep 2026", 1499.0, "Cancelled")
    )

    private val samplePayments = mutableListOf(
        PaymentItem("TXN-984210", "Rahul Menon", "Yearly", 11999.0, "Razorpay UPI", "Successful", "Today, 10:24 AM", orderId = "ORD_88421", paymentId = "PAY_77341", paidAt = "2026-09-24 10:24:12"),
        PaymentItem("TXN-984209", "Ananya Nair", "Monthly", 1499.0, "Google Pay", "Successful", "Today, 09:15 AM", orderId = "ORD_88420", paymentId = "PAY_77340", paidAt = "2026-09-24 09:15:45"),
        PaymentItem("TXN-984208", "Arjun Prasad", "Yearly", 11999.0, "Card / Razorpay", "Pending", "Yesterday, 07:40 PM", orderId = "ORD_88419", paymentId = "PAY_77339", paidAt = "Pending"),
        PaymentItem("TXN-984207", "Mohammed Faizal", "Half Yearly", 6999.0, "PayTM UPI", "Successful", "22 Sep 2026", orderId = "ORD_88418", paymentId = "PAY_77338", paidAt = "2026-09-22 18:22:04"),
        PaymentItem("TXN-984206", "Karthik Suresh", "Quarterly", 3999.0, "Net Banking", "Failed", "21 Sep 2026", orderId = "ORD_88417", paymentId = "PAY_77337", paidAt = "Failed - Bank Timeout"),
        PaymentItem("TXN-984205", "Vivek Raj", "Half Yearly", 6999.0, "PhonePe", "Successful", "20 Sep 2026", orderId = "ORD_88416", paymentId = "PAY_77336", paidAt = "2026-09-20 14:10:00")
    )

    private val sampleBookings = mutableListOf(
        BookingItem("b1", "Rahul Menon", "+91 98471 22334", "Yearly Plan", "Today", "06:00 PM - 07:30 PM", "Upcoming"),
        BookingItem("b2", "Ananya Nair", "+91 97455 33445", "Monthly Plan", "Today", "07:00 PM - 08:30 PM", "Upcoming"),
        BookingItem("b3", "Mohammed Faizal", "+91 99951 44556", "Half Yearly", "Today", "08:00 AM - 09:30 AM", "Completed"),
        BookingItem("b4", "Deepak Varma", "+91 94471 55667", "Quarterly Plan", "Today", "09:30 AM - 11:00 AM", "Completed"),
        BookingItem("b5", "Sneha Joseph", "+91 96331 66778", "Monthly Plan", "Tomorrow", "06:30 AM - 08:00 AM", "Upcoming"),
        BookingItem("b6", "Gautham Das", "+91 98950 11447", "Day Pass", "Today", "04:00 PM - 05:30 PM", "Cancelled")
    )

    private val sampleStaff = mutableListOf(
        StaffMember("st1", "Sanjay Nambiar", "sanjay@ironhousefitness.com", "+91 98472 00112", "Gym Admin", "Active", "12 Jan 2025"),
        StaffMember("st2", "Kavya Sreedharan", "kavya@ironhousefitness.com", "+91 97451 11223", "Staff", "Active", "15 Mar 2025"),
        StaffMember("st3", "Manoj K.V.", "manoj@ironhousefitness.com", "+91 99953 22334", "Staff", "Active", "01 Jun 2025"),
        StaffMember("st4", "Akhil George", "akhil@ironhousefitness.com", "+91 94474 33445", "Staff", "Inactive", "10 Sep 2025")
    )

    private val sampleNotifications = mutableListOf(
        NotificationItem("n1", "Payment Received", "Rahul Menon renewed Yearly Membership for ₹11,999 via Razorpay UPI.", "Payment", "10 mins ago", false),
        NotificationItem("n2", "New Check-in Booking", "Ananya Nair booked a workout slot for 07:00 PM today.", "Booking", "35 mins ago", false),
        NotificationItem("n3", "Membership Expiring Soon", "Sneha Joseph's Monthly plan expires in 2 days.", "Membership", "2 hours ago", false),
        NotificationItem("n4", "System Maintenance", "Scheduled BYCE cloud synchronization at 02:00 AM IST.", "System", "1 day ago", true),
        NotificationItem("n5", "Payment Failed", "Transaction TXN-984206 from Karthik Suresh timed out.", "Payment", "3 days ago", true)
    )

    // GET /api/v1/admin/dashboard/
    fun getDashboardKpis(): DashboardKpis = DashboardKpis(
        totalCustomers = 1248,
        totalCustomersGrowth = 12.0,
        activeMemberships = 986,
        activeMembershipsGrowth = 8.0,
        monthlyRevenue = 482500.0,
        monthlyRevenueGrowth = 15.4,
        upcomingBookings = 42
    )

    fun getRevenueChartData(filter: String = "30 Days"): List<RevenueDataPoint> {
        return when (filter) {
            "7 Days" -> listOf(
                RevenueDataPoint("Mon", 14200.0),
                RevenueDataPoint("Tue", 18500.0),
                RevenueDataPoint("Wed", 16800.0),
                RevenueDataPoint("Thu", 21400.0),
                RevenueDataPoint("Fri", 25900.0),
                RevenueDataPoint("Sat", 32100.0),
                RevenueDataPoint("Sun", 28400.0)
            )
            "3 Months" -> listOf(
                RevenueDataPoint("Jul", 420000.0),
                RevenueDataPoint("Aug", 456000.0),
                RevenueDataPoint("Sep", 482500.0)
            )
            "1 Year" -> listOf(
                RevenueDataPoint("Q1", 1180000.0),
                RevenueDataPoint("Q2", 1340000.0),
                RevenueDataPoint("Q3", 1420000.0),
                RevenueDataPoint("Q4", 1580000.0)
            )
            else -> listOf( // 30 Days
                RevenueDataPoint("Week 1", 108000.0),
                RevenueDataPoint("Week 2", 119500.0),
                RevenueDataPoint("Week 3", 124200.0),
                RevenueDataPoint("Week 4", 130800.0)
            )
        }
    }

    fun getMembershipOverview(): MembershipOverview = MembershipOverview(
        active = 986,
        pending = 45,
        expired = 172,
        cancelled = 45
    )

    // GET /api/v1/auth/me/
    fun getOwnerProfile(): GymOwnerUser = currentOwner

    fun updateOwnerProfile(name: String, email: String, phone: String) {
        currentOwner = currentOwner.copy(name = name, email = email, phone = phone)
    }

    // GET /api/v1/gyms/{id}/
    fun getGymProfile(): GymProfile = currentGym

    fun updateGymProfile(updated: GymProfile) {
        currentGym = updated
    }

    // GET /api/v1/admin/customers/
    fun getCustomers(): List<CustomerItem> = sampleCustomers

    fun addCustomer(customer: CustomerItem) {
        sampleCustomers.add(0, customer)
    }

    // GET /api/v1/admin/memberships/
    fun getMemberships(): List<MembershipItem> = sampleMemberships

    // GET /api/v1/gyms/{gym_id}/membership-plans/
    fun getMembershipPlans(): List<MembershipPlanItem> = samplePlans

    fun addPlan(plan: MembershipPlanItem) {
        samplePlans.add(plan)
    }

    fun togglePlanStatus(planId: String) {
        val index = samplePlans.indexOfFirst { it.id == planId }
        if (index != -1) {
            val p = samplePlans[index]
            samplePlans[index] = p.copy(isActive = !p.isActive)
        }
    }

    // GET /api/v1/admin/payments/
    fun getPayments(): List<PaymentItem> = samplePayments

    // GET /api/v1/admin/bookings/
    fun getBookings(): List<BookingItem> = sampleBookings

    fun cancelBooking(bookingId: String) {
        val index = sampleBookings.indexOfFirst { it.id == bookingId }
        if (index != -1) {
            sampleBookings[index] = sampleBookings[index].copy(status = "Cancelled")
        }
    }

    // Staff
    fun getStaff(): List<StaffMember> = sampleStaff

    fun addStaff(staff: StaffMember) {
        sampleStaff.add(0, staff)
    }

    fun toggleStaffStatus(staffId: String) {
        val index = sampleStaff.indexOfFirst { it.id == staffId }
        if (index != -1) {
            val s = sampleStaff[index]
            val newStatus = if (s.status == "Active") "Inactive" else "Active"
            sampleStaff[index] = s.copy(status = newStatus)
        }
    }

    // Notifications
    fun getNotifications(): List<NotificationItem> = sampleNotifications

    fun markNotificationAsRead(id: String) {
        val index = sampleNotifications.indexOfFirst { it.id == id }
        if (index != -1) {
            sampleNotifications[index] = sampleNotifications[index].copy(isRead = true)
        }
    }

    fun markAllNotificationsAsRead() {
        for (i in sampleNotifications.indices) {
            sampleNotifications[i] = sampleNotifications[i].copy(isRead = true)
        }
    }
}
