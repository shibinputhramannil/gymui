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
import com.example.data.models.CustomerItem
import com.example.data.services.GymOwnerRepository
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun OwnerCustomersScreen(
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatusFilter by remember { mutableStateOf("All") }
    val allCustomers = remember { GymOwnerRepository.getCustomers() }

    var selectedCustomerForDetail by remember { mutableStateOf<CustomerItem?>(null) }
    var showAddCustomerDialog by remember { mutableStateOf(false) }

    val filteredCustomers = remember(searchQuery, selectedStatusFilter, allCustomers) {
        allCustomers.filter { customer ->
            val matchesQuery = customer.name.contains(searchQuery, ignoreCase = true) ||
                    customer.email.contains(searchQuery, ignoreCase = true) ||
                    customer.phone.contains(searchQuery)
            val matchesStatus = if (selectedStatusFilter == "All") true else customer.status.equals(selectedStatusFilter, ignoreCase = true)
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "CUSTOMERS DIRECTORY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ByceNeonGreen,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Customer Management",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0xFF6B9330), Color(0xFF4E7320))
                                )
                            )
                            .clickable { showAddCustomerDialog = true }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Customer",
                                tint = TextWhite,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Add Customer",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OwnerSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    placeholder = "Search customers by name, email, phone...",
                    filterOptions = listOf("All", "Active", "Pending", "Expired"),
                    selectedFilter = selectedStatusFilter,
                    onFilterSelected = { selectedStatusFilter = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Showing ${filteredCustomers.size} customers",
                    fontSize = 12.sp,
                    color = ByceCoolGray
                )

                Spacer(modifier = Modifier.height(6.dp))
            }

            if (filteredCustomers.isEmpty()) {
                item {
                    OwnerEmptyState(
                        title = "No Customers Found",
                        description = "No customers matched your filter '$selectedStatusFilter' or search query."
                    )
                }
            } else {
                items(filteredCustomers, key = { it.id }) { customer ->
                    CustomerCardItem(
                        customer = customer,
                        onViewClick = { selectedCustomerForDetail = customer },
                        onEditClick = { selectedCustomerForDetail = customer }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        // Customer Details Modal
        selectedCustomerForDetail?.let { customer ->
            CustomerDetailDialog(
                customer = customer,
                onDismiss = { selectedCustomerForDetail = null }
            )
        }

        // Add Customer Modal
        if (showAddCustomerDialog) {
            AddCustomerDialog(
                onDismiss = { showAddCustomerDialog = false },
                onAdd = { newCustomer ->
                    GymOwnerRepository.addCustomer(newCustomer)
                    showAddCustomerDialog = false
                }
            )
        }
    }
}

@Composable
private fun CustomerCardItem(
    customer: CustomerItem,
    onViewClick: () -> Unit,
    onEditClick: () -> Unit
) {
    LiquidGlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0x28A6CE39))
                        .border(1.dp, GlassBorderLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = customer.avatarInitials,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ByceNeonGreen
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = customer.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        OwnerStatusBadge(status = customer.status)
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = customer.email,
                        fontSize = 12.sp,
                        color = ByceCoolGray
                    )
                    Text(
                        text = customer.phone,
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0x18FFFFFF))
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Plan: ${customer.membershipPlan}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextWhite
                    )
                    Text(
                        text = "Valid: ${customer.startDate} - ${customer.endDate}",
                        fontSize = 11.sp,
                        color = ByceCoolGray
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

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x15FFFFFF))
                            .clickable { onEditClick() }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Edit",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextWhite
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomerDetailDialog(
    customer: CustomerItem,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp)
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
                    Text(
                        text = "Customer Profile",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color(0x30A6CE39)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = customer.avatarInitials,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = ByceNeonGreen
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = customer.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(text = customer.email, fontSize = 12.sp, color = ByceCoolGray)
                        Text(text = customer.phone, fontSize = 12.sp, color = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x20FFFFFF)))
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "MEMBERSHIP DETAILS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ByceNeonGreen,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Current Plan", fontSize = 13.sp, color = TextMuted)
                    Text(text = customer.membershipPlan, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Status", fontSize = 13.sp, color = TextMuted)
                    OwnerStatusBadge(status = customer.status)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Start Date", fontSize = 13.sp, color = TextMuted)
                    Text(text = customer.startDate, fontSize = 13.sp, color = TextWhite)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "End Date", fontSize = 13.sp, color = TextMuted)
                    Text(text = customer.endDate, fontSize = 13.sp, color = TextWhite)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x20FFFFFF)))
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "RECENT BOOKINGS & ACTIVITY",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ByceNeonGreen,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Last checked in: Yesterday, 07:15 AM · Iron House Gym",
                    fontSize = 12.sp,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(20.dp))

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
                    Text(text = "Close Profile", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
            }
        }
    }
}

@Composable
private fun AddCustomerDialog(
    onDismiss: () -> Unit,
    onAdd: (CustomerItem) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var plan by remember { mutableStateOf("Monthly") }

    Dialog(onDismissRequest = onDismiss) {
        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp)
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
                    Text(
                        text = "Add New Customer",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LiquidGlassTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Full Name",
                    placeholder = "e.g. Anand Sharma"
                )

                Spacer(modifier = Modifier.height(10.dp))

                LiquidGlassTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email Address",
                    placeholder = "e.g. anand@gmail.com"
                )

                Spacer(modifier = Modifier.height(10.dp))

                LiquidGlassTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = "Phone Number",
                    placeholder = "+91 98470 00000"
                )

                Spacer(modifier = Modifier.height(18.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF6B9330), Color(0xFF4E7320))
                            )
                        )
                        .clickable {
                            if (name.isNotBlank()) {
                                val initials = name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("").uppercase()
                                onAdd(
                                    CustomerItem(
                                        id = "c_${System.currentTimeMillis()}",
                                        name = name,
                                        email = email.ifBlank { "member@byce.in" },
                                        phone = phone.ifBlank { "+91 98470 12345" },
                                        membershipPlan = plan,
                                        startDate = "24 Sep 2026",
                                        endDate = "23 Oct 2026",
                                        status = "Active",
                                        joinedDate = "24 Sep 2026",
                                        avatarInitials = initials.ifBlank { "MB" }
                                    )
                                )
                            }
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Register Customer",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
            }
        }
    }
}
