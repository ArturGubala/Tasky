package com.example.tasky.core.presentation.designsystem.pickers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.core.presentation.designsystem.theme.Inter
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.designsystem.theme.extended
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HorizontalDatePicker(
    modifier: Modifier = Modifier,
    selectedDate: ZonedDateTime,
    onDateSelected: (ZonedDateTime) -> Unit,
) {
    val today = ZonedDateTime.now()
    val startDate = today.minusDays(15)

    val dates = remember {
        (0..30).map { startDate.plusDays(it.toLong()) }
    }

//    var selectedDate by remember { mutableStateOf(today) }
    val listState = rememberLazyListState()

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val density = LocalDensity.current
        val itemWidthDp = 40.dp + 10.dp
        val horizontalPaddingDp = 16.dp
        val screenWidthDp = maxWidth

        val centerOffsetPx = with(density) {
            (screenWidthDp / 2 - itemWidthDp / 2 - horizontalPaddingDp).toPx()
        }

        LaunchedEffect(selectedDate) {
            val selectedIndex =
                dates.indexOfFirst { it.toLocalDate() == selectedDate.toLocalDate() }
            if (selectedIndex != -1) {
                listState.animateScrollToItem(
                    index = selectedIndex,
                    scrollOffset = -centerOffsetPx.toInt()
                )
            }
        }

        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(dates.size) { index ->
                val date = dates[index]
                val isSelected = date.toLocalDate() == selectedDate.toLocalDate()

                DatePill(
                    date = date,
                    isSelected = isSelected,
                    onClick = {
                        onDateSelected(date)
                    }
                )
            }
        }
    }
}

@Composable
private fun DatePill(
    date: ZonedDateTime,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(40.dp)
            .height(60.dp)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.extended.supplementary
                else Color.Transparent,
                shape = RoundedCornerShape(percent = 100)
            )
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).first()
                .toString(),
            fontSize = 11.sp,
            color = if (isSelected) MaterialTheme.colorScheme.background
            else MaterialTheme.colorScheme.onSurfaceVariant,
            fontFamily = Inter,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = date.dayOfMonth.toString(),
            color = if (isSelected) MaterialTheme.colorScheme.background
            else MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )
    }
}

@PreviewLightDark
@Composable
private fun HorizontalDatePickerPreview() {
    TaskyTheme {
        HorizontalDatePicker(
            selectedDate = ZonedDateTime.now(),
            onDateSelected = { selectedDate ->
                println("Selected date: $selectedDate")
            }
        )
    }
}
