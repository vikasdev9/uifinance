package com.uifinance.project291.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uifinance.project291.design_system.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerBottomSheet(
    initialDate: Date,
    onDateSelected: (Date) -> Unit,
    onDismiss: () -> Unit
) {
    var currentMonth by remember { mutableStateOf(Calendar.getInstance().apply { time = initialDate }) }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance().apply { time = initialDate }) }

    AppModalBottomSheet(
        visible = true,
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    currentMonth = (currentMonth.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
                }) {
                    Icon(Icons.AutoMirrored.Rounded.KeyboardArrowLeft, contentDescription = "Prev", tint = MutedGold)
                }

                Text(
                    text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(currentMonth.time),
                    style = MaterialTheme.typography.titleMedium,
                    color = HighEmphasisText,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = {
                    currentMonth = (currentMonth.clone() as Calendar).apply { add(Calendar.MONTH, 1) }
                }) {
                    Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = "Next", tint = MutedGold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Day Labels
            Row(modifier = Modifier.fillMaxWidth()) {
                val days = listOf("S", "M", "T", "W", "T", "F", "S")
                days.forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        color = SecondaryText
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Calendar Grid
            val calendarDays = remember(currentMonth) { getDaysInMonth(currentMonth) }
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.height(280.dp)
            ) {
                items(calendarDays) { date ->
                    if (date == null) {
                        Box(modifier = Modifier.aspectRatio(1f))
                    } else {
                        val isSelected = isSameDay(date, selectedDate.time)
                        val isToday = isSameDay(date, Date())
                        
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) MutedGold else Color.Transparent)
                                .clickable {
                                    selectedDate = Calendar.getInstance().apply { time = date }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = Calendar.getInstance().apply { time = date }.get(Calendar.DAY_OF_MONTH).toString(),
                                color = if (isSelected) DeepObsidian else if (isToday) MutedGold else HighEmphasisText,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { 
                    onDateSelected(selectedDate.time)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MutedGold),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("DONE", color = DeepObsidian, fontWeight = FontWeight.Bold)
            }
        }
    }
}

private fun getDaysInMonth(calendar: Calendar): List<Date?> {
    val monthCalendar = calendar.clone() as Calendar
    monthCalendar.set(Calendar.DAY_OF_MONTH, 1)
    val firstDayOfWeek = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1
    val daysInMonth = monthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    
    val days = mutableListOf<Date?>()
    for (i in 0 until firstDayOfWeek) {
        days.add(null)
    }
    for (i in 1..daysInMonth) {
        monthCalendar.set(Calendar.DAY_OF_MONTH, i)
        days.add(monthCalendar.time)
    }
    return days
}

private fun isSameDay(date1: Date, date2: Date): Boolean {
    val cal1 = Calendar.getInstance().apply { time = date1 }
    val cal2 = Calendar.getInstance().apply { time = date2 }
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
           cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}
