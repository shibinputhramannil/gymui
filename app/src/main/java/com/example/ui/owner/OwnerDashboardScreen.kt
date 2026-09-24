package com.example.ui.owner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.*
import com.example.data.services.GymOwnerRepository
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun OwnerDashboardScreen(
    onNavigateToCustomers: () -> Unit = {},
    onNavigateToMemberships: () -> Unit = {},
    onNavigateToPlans: () -> Unit = {},
    onNavigateToPayments: () -> Unit = {},
    onNavigateToBookings: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val kpis = remember { GymOwnerRepository.getDashboardKpis() }
    val membershipOverview = remember { GymOwnerRepository.getMembershipOverview() }
    var selectedRevenueFilter by remember { mutableStateOf("30 Days") }
    val revenueData = remember(selectedRevenueFilter) {
        GymOwnerRepository.getRevenueChartData(selectedRevenueFilter)
    }

    val recentCustomers = remember { GymOwnerRepository.getCustomers().take(4) }
    val recentPayments = remember { GymOwnerRepository.getPayments().take(4) }
    val upcomingBookings = remember { GymOwnerRepository.getBookings().take(3) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // ----------------------------------------------------
        // WELCOME BANNER
        // ----------------------------------------------------
        LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "WELCOME BACK",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ByceNeonGreen,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Kishore Kumar",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x20FFFFFF))
                            .border(1.dp, GlassBorderLight, RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Thu, 24 Sep 2026",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextWhite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Here is the summary of your gym's operational metrics, customer memberships, and revenue flow for today.",
                    fontSize = 13.sp,
                    color = TextMuted,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ----------------------------------------------------
        // KPI METRIC CARDS (2x2 Grid)
        // ----------------------------------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OwnerKpiCard(
                title = "Total Customers",
                value = "1,248",
                changeText = "+12% vs last mo",
                icon = Icons.Default.People,
                modifier = Modifier.weight(1f)
            )
            OwnerKpiCard(
                title = "Active Members",
                value = "986",
                changeText = "+8% vs last mo",
                icon = Icons.Default.CardMembership,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OwnerKpiCard(
                title = "Monthly Revenue",
                value = "₹4,82,500",
                changeText = "+15.4% vs last mo",
                icon = Icons.Default.CurrencyRupee,
                modifier = Modifier.weight(1f)
            )
            OwnerKpiCard(
                title = "Bookings Today",
                value = "42",
                changeText = "On schedule",
                icon = Icons.Default.EventAvailable,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ----------------------------------------------------
        // QUICK ACTIONS
        // ----------------------------------------------------
        OwnerSectionHeader(title = "Quick Actions")
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionButton(
                label = "Add Customer",
                icon = Icons.Default.PersonAdd,
                onClick = onNavigateToCustomers,
                modifier = Modifier.weight(1f)
            )
            QuickActionButton(
                label = "Create Plan",
                icon = Icons.Default.AddCard,
                onClick = onNavigateToPlans,
                modifier = Modifier.weight(1f)
            )
            QuickActionButton(
                label = "View Payments",
                icon = Icons.Default.ReceiptLong,
                onClick = onNavigateToPayments,
                modifier = Modifier.weight(1f)
            )
            QuickActionButton(
                label = "Bookings",
                icon = Icons.Default.CalendarToday,
                onClick = onNavigateToBookings,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ----------------------------------------------------
        // REVENUE OVERVIEW & CHART
        // ----------------------------------------------------
        OwnerSectionHeader(
            title = "Revenue Overview",
            actionLabel = "Detailed Report",
            onActionClick = onNavigateToPayments
        )
        Spacer(modifier = Modifier.height(8.dp))

        LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Filter Tabs (7 Days, 30 Days, 3 Months, 1 Year)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("7 Days", "30 Days", "3 Months", "1 Year").forEach { tab ->
                        val isSelected = selectedRevenueFilter == tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) ByceNeonGreen.copy(alpha = 0.25f) else Color(0x15FFFFFF))
                                .border(1.dp, if (isSelected) ByceNeonGreen else Color.Transparent, RoundedCornerShape(12.dp))
                                .clickable { selectedRevenueFilter = tab }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) ByceNeonGreen else TextMuted
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Bar Chart Visualizer
                val maxAmount = revenueData.maxOfOrNull { it.amount } ?: 1.0
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .padding(top = 10.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    revenueData.forEach { point ->
                        val barRatio = (point.amount / maxAmount).toFloat().coerceIn(0.1f, 1.0f)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "₹${(point.amount / 1000).toInt()}k",
                                fontSize = 10.sp,
                                color = ByceNeonGreen,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.55f)
                                    .fillMaxHeight(barRatio)
                                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(ByceNeonGreen, ByceDeepGreen)
                                        )
                                    )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = point.label,
                                fontSize = 10.sp,
                                color = ByceCoolGray,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ----------------------------------------------------
        // MEMBERSHIP OVERVIEW
        // ----------------------------------------------------
        OwnerSectionHeader(
            title = "Membership Overview",
            actionLabel = "All Memberships",
            onActionClick = onNavigateToMemberships
        )
        Spacer(modifier = Modifier.height(8.dp))

        LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Breakdown Badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MembershipStatusCounter("Active", membershipOverview.active.toString(), StatusActiveGreen)
                    MembershipStatusCounter("Pending", membershipOverview.pending.toString(), StatusPendingAmber)
                    MembershipStatusCounter("Expired", membershipOverview.expired.toString(), StatusExpiredRed)
                    MembershipStatusCounter("Cancelled", membershipOverview.cancelled.toString(), StatusCancelledGray)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Distribution Bar
                val total = (membershipOverview.active + membershipOverview.pending + membershipOverview.expired + membershipOverview.cancelled).toFloat()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                ) {
                    Box(modifier = Modifier.weight(membershipOverview.active / total).fillMaxHeight().background(StatusActiveGreen))
                    Box(modifier = Modifier.weight(membershipOverview.pending / total).fillMaxHeight().background(StatusPendingAmber))
                    Box(modifier = Modifier.weight(membershipOverview.expired / total).fillMaxHeight().background(StatusExpiredRed))
                    Box(modifier = Modifier.weight(membershipOverview.cancelled / total).fillMaxHeight().background(StatusCancelledGray))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ----------------------------------------------------
        // RECENT CUSTOMERS
        // ----------------------------------------------------
        OwnerSectionHeader(
            title = "Recent Customers",
            actionLabel = "View All",
            onActionClick = onNavigateToCustomers
        )
        Spacer(modifier = Modifier.height(8.dp))

        recentCustomers.forEach { customer ->
            LiquidGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(ByceNeonGreen.copy(alpha = 0.20f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = customer.avatarInitials,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ByceNeonGreen
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = customer.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextWhite
                        )
                        Text(
                            text = "${customer.membershipPlan} · Joined ${customer.joinedDate}",
                            fontSize = 11.sp,
                            color = ByceCoolGray
                        )
                    }

                    OwnerStatusBadge(status = customer.status)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ----------------------------------------------------
        // RECENT PAYMENTS
        // ----------------------------------------------------
        OwnerSectionHeader(
            title = "Recent Payments",
            actionLabel = "View All",
            onActionClick = onNavigateToPayments
        )
        Spacer(modifier = Modifier.height(8.dp))

        recentPayments.forEach { payment ->
            LiquidGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0x20FFFFFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CurrencyRupee,
                            contentDescription = "Payment",
                            tint = ByceNeonGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = payment.customerName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextWhite
                        )
                        Text(
                            text = "${payment.planName} · ${payment.date}",
                            fontSize = 11.sp,
                            color = ByceCoolGray
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "₹${payment.amount.toInt()}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        OwnerStatusBadge(status = payment.status)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ----------------------------------------------------
        // UPCOMING BOOKINGS
        // ----------------------------------------------------
        OwnerSectionHeader(
            title = "Upcoming Bookings",
            actionLabel = "View All",
            onActionClick = onNavigateToBookings
        )
        Spacer(modifier = Modifier.height(8.dp))

        upcomingBookings.forEach { booking ->
            LiquidGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0x20FFFFFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Time",
                            tint = ByceNeonGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = booking.customerName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextWhite
                        )
                        Text(
                            text = "${booking.date} · ${booking.time}",
                            fontSize = 11.sp,
                            color = ByceCoolGray
                        )
                    }

                    OwnerStatusBadge(status = booking.status)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun QuickActionButton(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LiquidGlassCard(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color(0x22FFFFFF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = TextWhite,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = TextWhite,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun MembershipStatusCounter(
    label: String,
    count: String,
    accentColor: Color
) {
    Column(horizontalAlignment = Alignment.Start) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(accentColor, CircleShape)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                color = ByceCoolGray
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = count,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )
    }
}
