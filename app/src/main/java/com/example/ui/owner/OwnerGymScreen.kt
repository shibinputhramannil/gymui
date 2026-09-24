package com.example.ui.owner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.models.GymProfile
import com.example.data.services.GymOwnerRepository
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun OwnerGymScreen(
    initialTab: String = "Profile",
    modifier: Modifier = Modifier
) {
    var activeSubTab by remember { mutableStateOf(initialTab) }
    var gymProfile by remember { mutableStateOf(GymOwnerRepository.getGymProfile()) }
    var showEditDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "FACILITY & GEO-LOCATION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ByceNeonGreen,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Gym Management",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0x20FFFFFF))
                    .clickable { showEditDialog = true }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = ByceNeonGreen, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Edit Gym", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ByceNeonGreen)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Subtabs: Profile | Location
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Profile", "Location").forEach { tab ->
                val isSelected = activeSubTab == tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) ByceNeonGreen else Color(0x18FFFFFF))
                        .clickable { activeSubTab = tab }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) DarkNavy else TextWhite
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        if (activeSubTab == "Profile") {
            // Gym Profile Information
            LiquidGlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.byce_logo),
                            contentDescription = "Gym Logo",
                            modifier = Modifier.height(38.dp)
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = gymProfile.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "${gymProfile.city}, ${gymProfile.state}",
                                fontSize = 12.sp,
                                color = ByceCoolGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(text = gymProfile.description, fontSize = 13.sp, color = TextMuted, lineHeight = 18.sp)

                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x18FFFFFF)))
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "OPERATING HOURS",
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
                        Text(text = "Daily Schedule", fontSize = 13.sp, color = TextMuted)
                        Text(
                            text = "${gymProfile.openingTime} - ${gymProfile.closingTime}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x18FFFFFF)))
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "CONTACT DETAILS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ByceNeonGreen,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ContactRow(Icons.Default.Phone, gymProfile.phone)
                    ContactRow(Icons.Default.Email, gymProfile.email)
                    ContactRow(Icons.Default.Language, gymProfile.website)

                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x18FFFFFF)))
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "ADDRESS & PINCODE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ByceNeonGreen,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "${gymProfile.address}\n${gymProfile.city}, ${gymProfile.district}\n${gymProfile.state} - ${gymProfile.pincode}", fontSize = 13.sp, color = TextWhite, lineHeight = 19.sp)
                }
            }
        } else {
            // Gym Location & Map View
            LiquidGlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "BYCE DISCOVERY GEO-LOCATION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ByceNeonGreen,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Nearby Gym Positioning",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Map View Container
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0xFF14241E), Color(0xFF0F1A15))
                                )
                            )
                            .border(1.dp, GlassBorderLight, RoundedCornerShape(18.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        // Simulated Dark Map Grid Lines
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            repeat(5) {
                                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x10FFFFFF)))
                            }
                        }

                        // Center Map Pin
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(ByceNeonGreen.copy(alpha = 0.2f))
                                    .border(1.dp, ByceNeonGreen, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Pin",
                                    tint = ByceNeonGreen,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(DarkNavyDepth)
                                    .border(0.5.dp, GlassBorderLight, RoundedCornerShape(10.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Iron House Fitness",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Coordinates Display
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Latitude", fontSize = 11.sp, color = ByceCoolGray)
                            Text(text = "${gymProfile.latitude}° N", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        }
                        Column {
                            Text(text = "Longitude", fontSize = 11.sp, color = ByceCoolGray)
                            Text(text = "${gymProfile.longitude}° E", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        }
                        Column {
                            Text(text = "Discovery Status", fontSize = 11.sp, color = ByceCoolGray)
                            OwnerStatusBadge(status = "Active")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x18FFFFFF)))
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Full Address",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ByceNeonGreen
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${gymProfile.address}, ${gymProfile.city}, ${gymProfile.district}, ${gymProfile.state} - ${gymProfile.pincode}",
                        fontSize = 13.sp,
                        color = TextMuted,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Edit Gym Dialog
        if (showEditDialog) {
            EditGymDialog(
                current = gymProfile,
                onDismiss = { showEditDialog = false },
                onSave = { updated ->
                    gymProfile = updated
                    GymOwnerRepository.updateGymProfile(updated)
                    showEditDialog = false
                }
            )
        }
    }
}

@Composable
private fun ContactRow(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = ByceNeonGreen, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = value, fontSize = 13.sp, color = TextWhite)
    }
}

@Composable
private fun EditGymDialog(
    current: GymProfile,
    onDismiss: () -> Unit,
    onSave: (GymProfile) -> Unit
) {
    var name by remember { mutableStateOf(current.name) }
    var description by remember { mutableStateOf(current.description) }
    var phone by remember { mutableStateOf(current.phone) }
    var email by remember { mutableStateOf(current.email) }
    var website by remember { mutableStateOf(current.website) }
    var address by remember { mutableStateOf(current.address) }
    var city by remember { mutableStateOf(current.city) }
    var state by remember { mutableStateOf(current.state) }
    var pincode by remember { mutableStateOf(current.pincode) }
    var openTime by remember { mutableStateOf(current.openingTime) }
    var closeTime by remember { mutableStateOf(current.closingTime) }

    Dialog(onDismissRequest = onDismiss) {
        LiquidGlassCard(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(androidx.compose.foundation.rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Edit Gym Information", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LiquidGlassTextField(value = name, onValueChange = { name = it }, label = "Gym Name")
                Spacer(modifier = Modifier.height(8.dp))
                LiquidGlassTextField(value = description, onValueChange = { description = it }, label = "Description")
                Spacer(modifier = Modifier.height(8.dp))
                LiquidGlassTextField(value = phone, onValueChange = { phone = it }, label = "Phone")
                Spacer(modifier = Modifier.height(8.dp))
                LiquidGlassTextField(value = email, onValueChange = { email = it }, label = "Email")
                Spacer(modifier = Modifier.height(8.dp))
                LiquidGlassTextField(value = website, onValueChange = { website = it }, label = "Website")
                Spacer(modifier = Modifier.height(8.dp))
                LiquidGlassTextField(value = address, onValueChange = { address = it }, label = "Address")
                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    LiquidGlassTextField(value = city, onValueChange = { city = it }, label = "City", modifier = Modifier.weight(1f))
                    LiquidGlassTextField(value = pincode, onValueChange = { pincode = it }, label = "Pincode", modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    LiquidGlassTextField(value = openTime, onValueChange = { openTime = it }, label = "Opening Time", modifier = Modifier.weight(1f))
                    LiquidGlassTextField(value = closeTime, onValueChange = { closeTime = it }, label = "Closing Time", modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(ByceNeonGreen)
                        .clickable {
                            onSave(
                                current.copy(
                                    name = name,
                                    description = description,
                                    phone = phone,
                                    email = email,
                                    website = website,
                                    address = address,
                                    city = city,
                                    state = state,
                                    pincode = pincode,
                                    openingTime = openTime,
                                    closingTime = closeTime
                                )
                            )
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Save Changes", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkNavy)
                }
            }
        }
    }
}
