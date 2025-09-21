@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tasky.agenda.presentation.agenda_detail

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tasky.R
import com.example.tasky.agenda.presentation.agenda_detail.AgendaDetailAction.OnDescriptionChange
import com.example.tasky.agenda.presentation.agenda_detail.AgendaDetailAction.OnTitleChange
import com.example.tasky.agenda.presentation.agenda_detail.components.AddAttendeeBottomSheetContent
import com.example.tasky.agenda.presentation.agenda_detail.components.DeleteBottomSheetContent
import com.example.tasky.agenda.presentation.util.AgendaDetailBottomSheetType
import com.example.tasky.agenda.presentation.util.AgendaDetailConfigProvider
import com.example.tasky.agenda.presentation.util.AgendaDetailView
import com.example.tasky.agenda.presentation.util.AgendaEditTextFieldType
import com.example.tasky.agenda.presentation.util.AgendaItemAttendeesStatus
import com.example.tasky.agenda.presentation.util.AgendaItemType
import com.example.tasky.agenda.presentation.util.AgendaTypeConfig
import com.example.tasky.agenda.presentation.util.DropdownTextAlignment
import com.example.tasky.agenda.presentation.util.MAX_NUMBER_OF_PHOTOS
import com.example.tasky.agenda.presentation.util.PhotoDetail
import com.example.tasky.agenda.presentation.util.PhotoDetailAction
import com.example.tasky.agenda.presentation.util.defaultAgendaItemIntervals
import com.example.tasky.agenda.presentation.util.rememberAgendaPhotoPickerLauncher
import com.example.tasky.core.data.util.AndroidImageCompressor.Companion.MAX_SIZE_BYTES
import com.example.tasky.core.presentation.designsystem.app_bars.TaskyTopAppBar
import com.example.tasky.core.presentation.designsystem.bottom_sheets.TaskyBottomSheet
import com.example.tasky.core.presentation.designsystem.buttons.TaskyAttendeeStatusRadioButton
import com.example.tasky.core.presentation.designsystem.buttons.TaskyTextButton
import com.example.tasky.core.presentation.designsystem.cards.TaskyAttendeeCard
import com.example.tasky.core.presentation.designsystem.containers.TaskyContentBox
import com.example.tasky.core.presentation.designsystem.drop_downs.TaskyAgendaItemDropdownMenu
import com.example.tasky.core.presentation.designsystem.icons.TaskyCircle
import com.example.tasky.core.presentation.designsystem.icons.TaskySquare
import com.example.tasky.core.presentation.designsystem.labels.TaskyLabel
import com.example.tasky.core.presentation.designsystem.layout.TaskyScaffold
import com.example.tasky.core.presentation.designsystem.pickers.TaskyDatePicker
import com.example.tasky.core.presentation.designsystem.pickers.TaskyPhotoPicker
import com.example.tasky.core.presentation.designsystem.pickers.TaskyTimePicker
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.designsystem.theme.extended
import com.example.tasky.core.presentation.ui.ObserveAsEvents
import com.example.tasky.core.presentation.ui.UiText
import com.example.tasky.core.presentation.util.DateTimeFormatter
import com.example.tasky.core.presentation.util.DeviceConfiguration
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.time.ZonedDateTime

@Composable
fun AgendaDetailScreenRoot(
    agendaItemType: AgendaItemType,
    agendaDetailView: AgendaDetailView,
    agendaId: String = "",
    returnedText: String? = null,
    editedFieldType: AgendaEditTextFieldType? = null,
    photoDetail: PhotoDetail,
    onNavigateBack: () -> Unit,
    onSwitchToReadOnly: () -> Unit,
    onNavigateToEdit: () -> Unit,
    onNavigateToEditText: (fieldType: AgendaEditTextFieldType,
                           text: String) -> Unit,
    onNavigateToPhotoDetail: (photoId: String, photoUri: String) -> Unit,
    viewModel: AgendaDetailViewModel = koinViewModel(
        parameters = { parametersOf(agendaId, agendaItemType) }
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val agendaItemTypeConfiguration by remember(agendaItemType) {
        mutableStateOf(AgendaDetailConfigProvider.getConfig(type = agendaItemType))
    }
    val isReadOnly = rememberSaveable { agendaDetailView == AgendaDetailView.READ_ONLY }

    val launchPhotoPicker = rememberAgendaPhotoPickerLauncher(
        onPhotoSelected = { uri ->
            viewModel.onAction(
        AgendaDetailAction.OnPhotoSelected(
                uriString = uri.toString(), maxBytes = MAX_SIZE_BYTES)
            )
        }
    )

    LaunchedEffect(returnedText) {
        editedFieldType?.let {
            when(editedFieldType) {
                AgendaEditTextFieldType.TITLE -> {
                    returnedText?.let { viewModel.onAction(OnTitleChange(title = it)) }
                }
                AgendaEditTextFieldType.DESCRIPTION -> {
                    returnedText?.let { viewModel.onAction(OnDescriptionChange(description = it)) }
                }
            }
        }
    }

    LaunchedEffect(photoDetail.photoDetailAction) {
        if (photoDetail.photoDetailAction == PhotoDetailAction.DELETE && photoDetail.photoId.isNotEmpty()) {
            viewModel.onAction(AgendaDetailAction.OnPhotoDelete(photoId = photoDetail.photoId))
        }
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is AgendaDetailEvent.ImageCompressFailure -> {
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
            is AgendaDetailEvent.ImageTooLarge -> {
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
            is AgendaDetailEvent.InvalidDatePicked -> {
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    AgendaDetailScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is AgendaDetailAction.OnEditTitleClick -> {
                    onNavigateToEditText(AgendaEditTextFieldType.TITLE, action.title)
                }
                is AgendaDetailAction.OnEditDescriptionClick -> {
                    onNavigateToEditText(AgendaEditTextFieldType.DESCRIPTION, action.description)
                }
                AgendaDetailAction.OnCancelClick -> {
                    if (isReadOnly) {
                        onSwitchToReadOnly()
                    } else {
                        onNavigateBack()
                    }
                }
                AgendaDetailAction.OnCloseClick -> {
                    onNavigateBack()
                }
                AgendaDetailAction.OnEditClick -> {
                    onNavigateToEdit()
                }
                AgendaDetailAction.OnAddPhotoClick -> {
                    if ((state.detailsAsEvent()?.photos?.size ?: 0) == MAX_NUMBER_OF_PHOTOS) {
                        Toast.makeText(
                            context,
                            UiText.StringResource(
                                id = R.string.max_number_of_photo_reach
                            ).asString(context),
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        launchPhotoPicker()
                    }
                }
                is AgendaDetailAction.OnPhotoClick -> {
                    onNavigateToPhotoDetail(action.photoId, action.uriString)
                }
                else -> viewModel.onAction(action)
            }
        },
        appBarTitle = agendaItemTypeConfiguration.getAppBarTitle(
            mode = agendaDetailView,
            context = context,
            itemDate = null
        ),
        deleteButtonText = agendaItemTypeConfiguration.getDeleteButtonText(context = context),
        agendaDetailView = agendaDetailView,
        agendaItemTypeConfiguration = agendaItemTypeConfiguration,
        isReadOnly = isReadOnly
    )
}

@Composable
fun AgendaDetailScreen(
    state: AgendaDetailState,
    onAction: (AgendaDetailAction) -> Unit,
    appBarTitle: String,
    deleteButtonText: String,
    agendaDetailView: AgendaDetailView,
    agendaItemTypeConfiguration: AgendaTypeConfig,
    isReadOnly: Boolean
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    TaskyScaffold(
        topBar = {
            when(agendaDetailView) {
                AgendaDetailView.READ_ONLY -> {
                    TaskyTopAppBar(
                        leftActions = {
                            TaskyTextButton(
                                onClick =  { onAction(AgendaDetailAction.OnCloseClick) }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Close icon",
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        },
                        rightActions = {
                            TaskyTextButton(
                                onClick =  { onAction(AgendaDetailAction.OnEditClick) }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Edit icon",
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        title = {
                            Text(
                                text = appBarTitle.uppercase(),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
                AgendaDetailView.EDIT -> {
                    TaskyTopAppBar(
                        leftActions = {
                            TaskyTextButton(
                                onClick = { onAction(AgendaDetailAction.OnCancelClick) }
                            ) {
                                Text(
                                    text = stringResource(R.string.cancel),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }
                        },
                        rightActions = {
                            TaskyTextButton(
                                onClick = {}
                            ) {
                                Text(
                                    text = stringResource(R.string.save),
                                    color = MaterialTheme.colorScheme.extended.success,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        title = {
                            Text(
                                text = appBarTitle.uppercase(),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    )
                }
            }
        }
    ) { padding ->
        TaskyContentBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.TopStart
        ) {
            BoxWithConstraints {
                val screenHeight = maxHeight
                val photoSectionBackgroundColor = MaterialTheme.colorScheme.extended.surfaceHigher

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = screenHeight)
                        .padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 48.dp)
                        .padding(WindowInsets.displayCutout.asPaddingValues())
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(28.dp)
                    ) {

                        // HEADER
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            TaskyLabel(
                                text = agendaItemTypeConfiguration.displayName.uppercase(),
                                textStyle = MaterialTheme.typography.labelMedium,
                                modifier = Modifier,
                                labelLeadingIcon = {
                                    TaskySquare(
                                        size = 20.dp,
                                        color = agendaItemTypeConfiguration.getColor(),
                                        modifier = Modifier
                                            .then(
                                                agendaItemTypeConfiguration.getStrokeColor()?.let { strokeColor ->
                                                    Modifier.border(
                                                        width = 1.dp,
                                                        color = strokeColor,
                                                        shape = RoundedCornerShape(4.dp))
                                                } ?: Modifier
                                            )
                                    )
                                }
                            )
                        }

                        Column {
                            // TITLE
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 24.dp)
                            ) {
                                TaskyLabel(
                                    text = state.title,
                                    textStyle = MaterialTheme.typography.headlineLarge,
                                    modifier = Modifier,
                                    labelLeadingIcon = {
                                        TaskyCircle(
                                            size = 20.dp,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            modifier = Modifier
                                        )
                                    }
                                )
                                if (!isReadOnly) {
                                    TaskyTextButton(
                                        onClick = {
                                            onAction(
                                                AgendaDetailAction.OnEditTitleClick(
                                                    title = state.title
                                                )
                                            )
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                                            contentDescription = "Arrow right",
                                            tint = MaterialTheme.colorScheme.extended.onSurfaceVariantOpacity70
                                        )
                                    }
                                }
                            }
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.extended.surfaceHigher
                            )
                            // DESCRIPTION
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 20.dp)
                            ) {
                                Text(
                                    text = state.description,
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                if (!isReadOnly) {
                                    TaskyTextButton(
                                        onClick = {
                                            onAction(
                                                AgendaDetailAction.OnEditDescriptionClick(
                                                    description = state.description
                                                )
                                            )
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                                            contentDescription = "Arrow right",
                                            tint = MaterialTheme.colorScheme.extended.onSurfaceVariantOpacity70
                                        )
                                    }
                                }
                            }

                            // PHOTO PICKER
                            if (agendaItemTypeConfiguration.type == AgendaItemType.EVENT &&
                                !isReadOnly) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .drawBehind {
                                            drawRect(
                                                color = photoSectionBackgroundColor,
                                                topLeft =
                                                    Offset(-16.dp.toPx(), 0f),
                                                size = Size(
                                                    width = size.width + 32.dp.toPx(), // Add back the horizontal padding
                                                    height = size.height
                                                )
                                            )
                                        }
                                        .padding(vertical = 20.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TaskyPhotoPicker(
                                        photos = state.detailsAsEvent()?.photos?.map {
                                            it.toUi()
                                        } ?: listOf(),
                                        onPhotoClick = { photoId, uriString ->
                                            onAction(
                                                AgendaDetailAction.OnPhotoClick(
                                                    photoId = photoId, uriString = uriString
                                                )
                                            )
                                        },
                                        onAddClick = { onAction(AgendaDetailAction.OnAddPhotoClick) },
                                        imageLoading = state.detailsAsEvent()?.isImageLoading ?: false,
                                        isReadOnly = isReadOnly,
                                        isOnline = state.isOnline,
                                        numberOfPhotoInRow = when (deviceConfiguration) {
                                            DeviceConfiguration.MOBILE_PORTRAIT -> 5
                                            else -> 10
                                        }
                                    )
                                }
                            } else {
                                HorizontalDivider(
                                    thickness = 1.dp,
                                    color = MaterialTheme.colorScheme.extended.surfaceHigher
                                )
                            }

                            // DATES
                            if (agendaItemTypeConfiguration.type == AgendaItemType.EVENT) {
                                // DATE FROM
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 20.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(R.string.from),
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        TaskyTimePicker(
                                            selectedTime = DateTimeFormatter.formatTaskyDetailPickerTime(
                                                hour = state.localFromTime.hour,
                                                minute = state.localFromTime.minute,
                                            ),
                                            onValueChange = { hour, minute ->
                                                onAction(
                                                    AgendaDetailAction.OnTimeFromPick(hour = hour, minute = minute)
                                                )
                                            },
                                            modifier = Modifier.requiredWidth(120.dp),
                                            isReadOnly = isReadOnly
                                        )
                                        TaskyDatePicker(
                                            selectedDate = DateTimeFormatter.formatTaskyDetailPickerDate(
                                                dateMillis = state.fromTime.toInstant().toEpochMilli()
                                            ),
                                            onValueChange = { dateMillis ->
                                                onAction(
                                                    AgendaDetailAction.OnDateFromPick(dateMillis = dateMillis)
                                                )
                                            },
                                            modifier = Modifier.requiredWidth(156.dp),
                                            isReadOnly = isReadOnly
                                        )
                                    }
                                }

                                HorizontalDivider(
                                    thickness = 1.dp,
                                    color = MaterialTheme.colorScheme.extended.surfaceHigher
                                )

                                // DATE TO
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 20.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(R.string.to),
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        TaskyTimePicker(
                                            selectedTime = DateTimeFormatter.formatTaskyDetailPickerTime(
                                                hour = state.detailsAsEvent()?.localToTime?.hour ?: 0,
                                                minute = state.detailsAsEvent()?.localToTime?.minute ?: 0,
                                            ),
                                            onValueChange = { hour, minute ->
                                                onAction(
                                                    AgendaDetailAction.OnTimeToPick(hour = hour, minute = minute)
                                                )
                                            },
                                            modifier = Modifier.requiredWidth(120.dp),
                                            isReadOnly = isReadOnly
                                        )
                                        TaskyDatePicker(
                                            selectedDate = DateTimeFormatter.formatTaskyDetailPickerDate(
                                                dateMillis = state.detailsAsEvent()?.localToTime
                                                    ?.toInstant()?.toEpochMilli() ?:
                                                ZonedDateTime.now().toInstant().toEpochMilli()
                                            ),
                                            onValueChange = { dateMillis ->
                                                onAction(
                                                    AgendaDetailAction.OnDateToPick(dateMillis = dateMillis)
                                                )
                                            },
                                            modifier = Modifier.requiredWidth(156.dp),
                                            isReadOnly = isReadOnly
                                        )
                                    }
                                }
                            } else {
                                // DATE AT
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 20.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(R.string.at),
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        TaskyTimePicker(
                                            selectedTime = DateTimeFormatter.formatTaskyDetailPickerTime(
                                                hour = state.localFromTime.hour,
                                                minute = state.localFromTime.minute,
                                            ),
                                            onValueChange = { hour, minute ->
                                                onAction(
                                                    AgendaDetailAction.OnTimeFromPick(hour = hour, minute = minute)
                                                )
                                            },
                                            modifier = Modifier.requiredWidth(120.dp),
                                            isReadOnly = isReadOnly
                                        )
                                        TaskyDatePicker(
                                            selectedDate = DateTimeFormatter.formatTaskyDetailPickerDate(
                                                dateMillis = state.fromTime.toInstant().toEpochMilli()
                                            ),
                                            onValueChange = { dateMillis ->
                                                onAction(
                                                    AgendaDetailAction.OnDateFromPick(dateMillis = dateMillis)
                                                )
                                            },
                                            modifier = Modifier.requiredWidth(156.dp),
                                            isReadOnly = isReadOnly
                                        )
                                    }
                                }
                            }

                            HorizontalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.extended.surfaceHigher
                            )

                            // REMAINDER TIME PICKER
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TaskyAgendaItemDropdownMenu(
                                    selectedReminder = state.selectedAgendaReminderInterval,
                                    availableIntervals = defaultAgendaItemIntervals(),
                                    onReminderSelected = {
                                        onAction(
                                            AgendaDetailAction.OnAgendaItemIntervalSelect(reminder = it)
                                        )
                                    },
                                    enabled = !isReadOnly,
                                    textAlignment = when (deviceConfiguration) {
                                        DeviceConfiguration.MOBILE_PORTRAIT -> DropdownTextAlignment.Start
                                        else -> DropdownTextAlignment.End
                                    }
                                )
                            }
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.extended.surfaceHigher
                            )

                            if (agendaItemTypeConfiguration.type == AgendaItemType.EVENT) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 28.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = if (state.isOnline)
                                            Arrangement.SpaceBetween else Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Visitors",
                                            color = MaterialTheme.colorScheme.primary,
                                            style = MaterialTheme.typography.headlineMedium
                                        )
                                        if (state.isOnline && !isReadOnly) {
                                            TaskyTextButton(
                                                onClick = { onAction(AgendaDetailAction.OnAddAttendeeClick) }
                                            ) {
                                                TaskySquare(
                                                    size = 32.dp,
                                                    color = MaterialTheme.colorScheme.extended.surfaceHigher
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Filled.Add,
                                                        contentDescription = "Close icon",
                                                        tint = MaterialTheme.colorScheme.onSurface
                                                    )
                                                }
                                            }
                                        } else if (!state.isOnline && !isReadOnly) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_offline),
                                                contentDescription = stringResource(R.string.offline_icon),
                                                modifier = Modifier.size(16.dp),
                                                tint = MaterialTheme.colorScheme.extended.onSurfaceVariantOpacity70
                                            )
                                        }
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                    ) {
                                        TaskyAttendeeStatusRadioButton(
                                            options = AgendaItemAttendeesStatus.entries,
                                            onOptionSelect = {
                                                onAction(AgendaDetailAction.OnAttendeeStatusClick(status = it))
                                            },
                                            selectedOption = state.selectedAttendeeStatus,
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = when (deviceConfiguration) {
                                                DeviceConfiguration.MOBILE_PORTRAIT -> Arrangement.SpaceBetween
                                                else -> Arrangement.spacedBy(8.dp)
                                            }
                                        )
                                    }

                                    val goingAttendees = state.detailsAsEvent()?.attendees?.filter { it.isGoing }
                                    if (goingAttendees?.isNotEmpty() ?: false && state.selectedAttendeeStatus != AgendaItemAttendeesStatus.NOT_GOING) {
                                        Text(
                                            text = "Going",
                                            color = MaterialTheme.colorScheme.onSurface,
                                            style = MaterialTheme.typography.headlineSmall
                                        )

                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            goingAttendees.forEach { attendee ->
                                                TaskyAttendeeCard(
                                                    attendeeName = attendee.username,
                                                    isCreator = attendee.isCreator,
                                                    canEdit = state.isOnline && !isReadOnly
                                                )
                                            }
                                        }
                                    }

                                    val notGoingAttendees = state.detailsAsEvent()?.attendees?.filter { !it.isGoing }
                                    if (notGoingAttendees?.isNotEmpty() ?: false && state.selectedAttendeeStatus != AgendaItemAttendeesStatus.GOING) {
                                        Text(
                                            text = "Not going",
                                            color = MaterialTheme.colorScheme.onSurface,
                                            style = MaterialTheme.typography.headlineSmall
                                        )

                                        Column {
                                            notGoingAttendees.forEach { attendee ->
                                                TaskyAttendeeCard(
                                                    attendeeName = attendee.username,
                                                    isCreator = attendee.isCreator,
                                                    canEdit = state.isOnline && !isReadOnly
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Column {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.extended.surfaceHigher
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            TaskyTextButton(
                                onClick = { onAction(AgendaDetailAction.OnDeleteAgendaItemClick) }
                            ) {
                                Text(
                                    text = stringResource(
                                        R.string.delete_context,
                                        agendaItemTypeConfiguration.displayName.uppercase()
                                    ),
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                }
            }
        }

        if (state.agendaDetailBottomSheetType != AgendaDetailBottomSheetType.NONE) {
            val sheetState = rememberModalBottomSheetState()
            val scope = rememberCoroutineScope()
            TaskyBottomSheet(
                onDismiss = { onAction(AgendaDetailAction.OnDismissBottomSheet) },
                sheetState = sheetState,
                content = {
                    when(state.agendaDetailBottomSheetType) {
                        AgendaDetailBottomSheetType.DELETE_AGENDA_ITEM -> {
                            DeleteBottomSheetContent(
                                onDelete = {},
                                onCancel = {
                                    scope.launch {
                                        sheetState.hide()
                                        onAction(AgendaDetailAction.OnDismissBottomSheet)
                                    }
                                },
                                title = deleteButtonText
                            )
                        }

                        AgendaDetailBottomSheetType.ADD_ATTENDEE -> {
                            AddAttendeeBottomSheetContent(
                                onCloseClick = {
                                    scope.launch {
                                        sheetState.hide()
                                        onAction(AgendaDetailAction.OnDismissBottomSheet)
                                    }
                                },
                                onAddClick = {},
                                attendeeEmail = state.detailsAsEvent()?.attendeeEmail ?: "",
                                isAttendeeEmailValid = state.detailsAsEvent()?.isAttendeeEmailValid ?: false,
                                isAttendeeEmailFieldFocused = state.detailsAsEvent()?.isAttendeeEmailFocused ?: false,
                                onAttendeeEmailChange = { email ->
                                    onAction(AgendaDetailAction.OnAttendeeEmailValueChanged(email = email))
                                },
                                onAttendeeEmailFieldFocusChange = { hasFocus ->
                                    onAction(AgendaDetailAction.OnAttendeeEmailFieldFocusChanged(
                                        hasFocus = hasFocus
                                    ))
                                },
                                errors = state.detailsAsEvent()?.errors ?: emptyList()
                            )
                        }

                        else -> Unit
                    }
                }
            )
        }
    }
}

@PreviewLightDark
@PreviewScreenSizes
@Composable
private fun AgendaDetailScreenPreview() {
    TaskyTheme {
        AgendaDetailScreen(
            state = AgendaDetailState(isOnline = true),
            onAction = {},
            appBarTitle = "Title",
            deleteButtonText = "Delete",
            agendaDetailView = AgendaDetailView.EDIT,
            agendaItemTypeConfiguration = AgendaDetailConfigProvider.getConfig(type = AgendaItemType.EVENT),
            isReadOnly = false
        )
    }
}
