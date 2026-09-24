package com.example.ui.owner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.models.BookingItem
import com.example.data.services.GymOwnerRepository
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun OwnerBookingsScreen(
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    val bookings = remember { mutableStateListOf<BookingItem>().apply { addAll(GymOwnerRepository.getBookings()) } }
    var selectedBookingForDetail by remember { mutableStateOf<BookingItem?>(null) }

    val filteredBookings = remember(searchQuery, selectedFilter, bookings.toList()) {
        bookings.filter { item ->
            val matchesQuery = item.customerName.contains(searchQuery, ignoreCase = true) ||
                    item.membershipPlan.contains(searchQuery, ignoreCase = true)
            val matchesFilter = when (selectedFilter) {
                "Today" -> item.date.equals("Today", ignoreCase = true)
                "Upcoming" -> item.status.equals("Upcoming", ignoreCase = true)
                "Completed" -> item.status.equals("Completed", ignoreCase = true)
                "Cancelled" -> item.status.equals("Cancelled", ignoreCase = true)
                else -> true
            }
            matchesQuery && matchesFilter
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = "CHECK-IN RESERVATIONS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ByceNeonGreen,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Member Slot Bookings",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(14.dp))

                OwnerSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    placeholder = "Search member booking...",
                    filterOptions = listOf("All", "Today", "Upcoming", "Completed", "Cancelled"),
                    selectedFilter = selectedFilter,
                    onFilterSelected = { selectedFilter = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Showing ${filteredBookings.size} bookings",
                    fontSize = 12.sp,
                    color = ByceCoolGray
                )

                Spacer(modifier = Modifier.height(4.dp))
            }

            if (filteredBookings.isEmpty()) {
                item {
                    OwnerEmptyState(
                        title = "No Bookings",
                        description = "No reservations match '$selectedFilter'."
                    )
                }
            } else {
                items(filteredBookings, key = { it.id }) { booking ->
                    BookingRowCard(
                        booking = booking,
                        onViewClick = { selectedBookingForDetail = booking },
                        onCancelClick = {
                            val index = bookings.indexOfFirst { it.id == booking.id }
                            if (index != -1) {
                                bookings[index] = booking.copy(status = "Cancelled")
                                GymOwnerRepository.cancelBooking(booking.id)
                            }
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        selectedBookingForDetail?.let { booking ->
            BookingDetailModal(
                booking = booking,
                onDismiss = { selectedBookingForDetail = null },
                onCancelBooking = {
                    val index = bookings.indexOfFirst { it.id == booking.id }
                    if (index != -1) {
                        bookings[index] = booking.copy(status = "Cancelled")
                        GymOwnerRepository.cancelBooking(booking.id)
                    }
                    selectedBookingForDetail = null
                }
            )
        }
    }
}

@Composable
private fun BookingRowCard(
    booking: BookingItem,
    onViewClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    LiquidGlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = booking.customerName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = booking.membershipPlan,
                        fontSize = 12.sp,
                        color = ByceCoolGray
                    )
                }
                OwnerStatusBadge(status = booking.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = ByceNeonGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${booking.date} · ${booking.time}",
                        fontSize = 12.sp,
                        color = TextWhite
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x20FFFFFF))
                            .clickable { onViewClick() }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "View",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ByceNeonGreen
                        )
                    }

                    if (booking.status == "Upcoming") {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(StatusExpiredRedBg)
                                .clickable { onCancelClick() }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Cancel",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = StatusExpiredRed
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BookingDetailModal(
    booking: BookingItem,
    onDismiss: () -> Unit,
    onCancelBooking: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        LiquidGlassCard(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Booking Details", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(text = booking.customerName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Text(text = "Plan: ${booking.membershipPlan}", fontSize = 12.sp, color = ByceCoolGray)

                Spacer(modifier = Modifier.height(14.dp))
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x20FFFFFF)))
                Spacer(modifier = Modifier.height(12.dp))

                BookingDetailRow("Booking ID", booking.id)
                BookingDetailRow("Scheduled Date", booking.date)
                BookingDetailRow("Slot Window", booking.time)
                BookingDetailRow("Gym Location", "Iron House Fitness, Kozhikode")

                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Status", fontSize = 13.sp, color = TextMuted)
                    OwnerStatusBadge(status = booking.status)
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (booking.status == "Upcoming") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(StatusExpiredRedBg)
                            .border(1.dp, StatusExpiredRed.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .clickable { onCancelBooking() }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Cancel Booking", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = StatusExpiredRed)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF6B9330), Color(0xFF4E7320))
                            )
                        )
                        .clickable { onDismiss() }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Close", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
            }
        }
    }
}

@Composable
private fun BookingDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = TextMuted)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
    }
}
