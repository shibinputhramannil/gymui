package com.example.ui.owner

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun OwnerReportsScreen(
    modifier: Modifier = Modifier
) {
    var selectedTimeframe by remember { mutableStateOf("Monthly") }
    var selectedReportSection by remember { mutableStateOf("Revenue") } // Revenue, Memberships, Customers

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "ANALYTICS & INSIGHTS",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = ByceNeonGreen,
            letterSpacing = 1.sp
        )
        Text(
            text = "Gym Performance Reports",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Timeframe selector (Daily, Weekly, Monthly, Yearly)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("Daily", "Weekly", "Monthly", "Yearly").forEach { tf ->
                val isSelected = selectedTimeframe == tf
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) ByceNeonGreen.copy(alpha = 0.25f) else Color(0x15FFFFFF))
                        .border(1.dp, if (isSelected) ByceNeonGreen else Color.Transparent, RoundedCornerShape(12.dp))
                        .clickable { selectedTimeframe = tf }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tf,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) ByceNeonGreen else TextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Section Pills: Revenue | Memberships | Customers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Revenue", "Memberships", "Customers").forEach { sec ->
                val isSelected = selectedReportSection == sec
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) ByceNeonGreen.copy(alpha = 0.25f) else Color(0x15FFFFFF))
                        .border(1.dp, if (isSelected) ByceNeonGreen else GlassBorderLight, RoundedCornerShape(14.dp))
                        .clickable { selectedReportSection = sec }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = sec,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) ByceNeonGreen else TextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        when (selectedReportSection) {
            "Revenue" -> {
                AnalyticsChartCard(
                    title = "Revenue Analytics ($selectedTimeframe)",
                    totalValue = "₹4,82,500",
                    growthText = "+15.4% growth",
                    dataPoints = listOf(
                        Pair("Week 1", 108.0),
                        Pair("Week 2", 119.5),
                        Pair("Week 3", 124.2),
                        Pair("Week 4", 130.8)
                    ),
                    unit = "k"
                )
            }
            "Memberships" -> {
                AnalyticsChartCard(
                    title = "Membership Retention & Growth",
                    totalValue = "986 Active",
                    growthText = "+8.2% new subscriptions",
                    dataPoints = listOf(
                        Pair("Basic", 340.0),
                        Pair("Monthly", 420.0),
                        Pair("Quarterly", 140.0),
                        Pair("Yearly", 86.0)
                    ),
                    unit = " members"
                )
            }
            "Customers" -> {
                AnalyticsChartCard(
                    title = "Customer Acquisition",
                    totalValue = "1,248 Total",
                    growthText = "+12% total registered",
                    dataPoints = listOf(
                        Pair("May", 950.0),
                        Pair("Jun", 1040.0),
                        Pair("Jul", 1120.0),
                        Pair("Aug", 1180.0),
                        Pair("Sep", 1248.0)
                    ),
                    unit = " users"
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Key Summary Breakdown
        LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Text(
                    text = "EXECUTIVE SUMMARY",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ByceNeonGreen,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                SummaryLine("Average Revenue / Member", "₹489.35 / mo")
                SummaryLine("Renewal Rate", "91.4%")
                SummaryLine("Check-in Slot Utilization", "78.2% peak hours")
                SummaryLine("Payment Gateway Success Rate", "99.1%")
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun AnalyticsChartCard(
    title: String,
    totalValue: String,
    growthText: String,
    dataPoints: List<Pair<String, Double>>,
    unit: String
) {
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
                    Text(text = title, fontSize = 13.sp, color = ByceCoolGray)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = totalValue, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(StatusActiveGreenBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = growthText, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ByceNeonGreen)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            val maxVal = dataPoints.maxOfOrNull { it.second } ?: 1.0
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                dataPoints.forEach { point ->
                    val ratio = (point.second / maxVal).toFloat().coerceIn(0.15f, 1.0f)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "${point.second.toInt()}$unit",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = ByceNeonGreen
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .fillMaxHeight(ratio)
                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(ByceNeonGreen, ByceDeepGreen)
                                    )
                                )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = point.first,
                            fontSize = 10.sp,
                            color = ByceCoolGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryLine(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = TextMuted)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
    }
}
