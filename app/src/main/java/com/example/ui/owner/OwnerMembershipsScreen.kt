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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.models.MembershipItem
import com.example.data.services.GymOwnerRepository
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun OwnerMembershipsScreen(
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatusFilter by remember { mutableStateOf("All") }
    val allMemberships = remember { GymOwnerRepository.getMemberships() }
    val overview = remember { GymOwnerRepository.getMembershipOverview() }
    var selectedMembershipForDetail by remember { mutableStateOf<MembershipItem?>(null) }

    val filteredMemberships = remember(searchQuery, selectedStatusFilter, allMemberships) {
        allMemberships.filter { item ->
            val matchesQuery = item.customerName.contains(searchQuery, ignoreCase = true) ||
                    item.planName.contains(searchQuery, ignoreCase = true)
            val matchesStatus = if (selectedStatusFilter == "All") true else item.status.equals(selectedStatusFilter, ignoreCase = true)
            matchesQuery && matchesStatus
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
                    text = "MEMBERSHIP PASSES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ByceNeonGreen,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Active & Expired Memberships",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Status counters strip
                LiquidGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatusCounterItem("Active", overview.active.toString(), StatusActiveGreen)
                        StatusCounterItem("Pending", overview.pending.toString(), StatusPendingAmber)
                        StatusCounterItem("Expired", overview.expired.toString(), StatusExpiredRed)
                        StatusCounterItem("Cancelled", overview.cancelled.toString(), StatusCancelledGray)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OwnerSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    placeholder = "Search membership by customer or plan...",
                    filterOptions = listOf("All", "Active", "Pending", "Expired", "Cancelled"),
                    selectedFilter = selectedStatusFilter,
                    onFilterSelected = { selectedStatusFilter = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Showing ${filteredMemberships.size} memberships",
                    fontSize = 12.sp,
                    color = ByceCoolGray
                )

                Spacer(modifier = Modifier.height(4.dp))
            }

            if (filteredMemberships.isEmpty()) {
                item {
                    OwnerEmptyState(
                        title = "No Memberships",
                        description = "No memberships match filter '$selectedStatusFilter'."
                    )
                }
            } else {
                items(filteredMemberships, key = { it.id }) { item ->
                    MembershipRowCard(
                        item = item,
                        onViewClick = { selectedMembershipForDetail = item }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        // Details Modal
        selectedMembershipForDetail?.let { item ->
            MembershipDetailModal(
                item = item,
                onDismiss = { selectedMembershipForDetail = null }
            )
        }
    }
}

@Composable
private fun StatusCounterItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = color)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, fontSize = 11.sp, color = ByceCoolGray)
    }
}

@Composable
private fun MembershipRowCard(
    item: MembershipItem,
    onViewClick: () -> Unit
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
                        text = item.customerName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = item.planName + " · " + item.gymName,
                        fontSize = 12.sp,
                        color = ByceCoolGray
                    )
                }
                OwnerStatusBadge(status = item.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Duration: ${item.startDate} - ${item.endDate}",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    Text(
                        text = "Amount: ₹${item.amount.toInt()}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ByceNeonGreen
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x20FFFFFF))
                        .clickable { onViewClick() }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Details",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ByceNeonGreen
                    )
                }
            }
        }
    }
}

@Composable
private fun MembershipDetailModal(
    item: MembershipItem,
    onDismiss: () -> Unit
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
                    Text(text = "Membership Details", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(text = "Customer: ${item.customerName}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Text(text = "Email: ${item.customerEmail}", fontSize = 12.sp, color = ByceCoolGray)

                Spacer(modifier = Modifier.height(14.dp))
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x20FFFFFF)))
                Spacer(modifier = Modifier.height(12.dp))

                DetailRow("Plan Name", item.planName)
                DetailRow("Facility", item.gymName)
                DetailRow("Start Date", item.startDate)
                DetailRow("End Date", item.endDate)
                DetailRow("Amount Paid", "₹${item.amount.toInt()}")

                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Status", fontSize = 13.sp, color = TextMuted)
                    OwnerStatusBadge(status = item.status)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(ByceNeonGreen)
                        .clickable { onDismiss() }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Close", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkNavy)
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = TextMuted)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
    }
}
