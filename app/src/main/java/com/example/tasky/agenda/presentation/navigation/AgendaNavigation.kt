package com.example.tasky.agenda.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.tasky.agenda.presentation.agenda_detail.AgendaDetailScreenRoot
import com.example.tasky.agenda.presentation.agenda_detail.photo_detail.PhotoDetailScreenRoot
import com.example.tasky.agenda.presentation.agenda_edit.AgendaEditTextScreenRoot
import com.example.tasky.agenda.presentation.agenda_list.AgendaScreenRoot
import com.example.tasky.agenda.presentation.util.AgendaDetailView
import com.example.tasky.agenda.presentation.util.AgendaEditTextFieldType
import com.example.tasky.agenda.presentation.util.AgendaItemType
import com.example.tasky.agenda.presentation.util.PhotoDetail
import com.example.tasky.agenda.presentation.util.PhotoDetailAction
import com.example.tasky.auth.presentation.navigation.navigateToLoginScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.agendaNavGraph(
    navController: NavHostController
) {
    navigation<AgendaGraph>(
        startDestination = AgendaListScreen,
    ) {
        agendaListScreen(navController = navController)
        agendaDetailScreen(navController = navController)
        agendaEditTextScreen(navController = navController)
        agendaPhotoDetailScreen(navController = navController)
    }
}

fun NavController.navigateToAgendaListScreen() = navigate(AgendaListScreen)

fun NavGraphBuilder.agendaListScreen(
    navController: NavController
) {
    composable<AgendaListScreen> {
        AgendaScreenRoot(
            onSuccessfulLogout = { navController.navigateToLoginScreen() },
            onFabMenuOptionClick = { agendaItemType, agendaDetailView, agendaId ->
                navController.navigateToAgendaDetailScreen(agendaItemType, agendaDetailView, agendaId)
            }
        )
    }
}

fun NavController.navigateToAgendaDetailScreen(agendaItemType: AgendaItemType,
                                               agendaDetailView: AgendaDetailView,
                                               agendaId: String)
= navigate(AgendaDetailScreen(
    agendaItemType = agendaItemType,
    agendaDetailView = agendaDetailView,
    agendaId = agendaId
))

fun NavGraphBuilder.agendaDetailScreen(
    navController: NavController
) {
    composable<AgendaDetailScreen> { entry ->
        val args = entry.toRoute<AgendaDetailScreen>()
        val text = entry.savedStateHandle.get<String>("text")
        val savedFieldType = entry.savedStateHandle.get<AgendaEditTextFieldType>("fieldType")
        val photoId = entry.savedStateHandle.get<String>("photoId")
        val photoDetailAction = entry.savedStateHandle.get<PhotoDetailAction>("photoDetailAction")
        val photoDetail = PhotoDetail(
            photoId = photoId ?: "", photoDetailAction = photoDetailAction ?: PhotoDetailAction.NONE
        )
        AgendaDetailScreenRoot(
            agendaItemType = args.agendaItemType,
            agendaDetailView = args.agendaDetailView,
            agendaId = args.agendaId,
            returnedText = text,
            editedFieldType = savedFieldType,
            photoDetail = photoDetail,
            onNavigateBack = {
                navController.popBackStack()
            },
            onSwitchToReadOnly = {
                navController.navigate(
                    AgendaDetailScreen(
                        agendaItemType = args.agendaItemType,
                        agendaDetailView = AgendaDetailView.READ_ONLY,
                        agendaId = args.agendaId
                    )
                ) {
                    popUpTo(
                        AgendaDetailScreen(
                            agendaItemType = args.agendaItemType,
                            agendaDetailView = args.agendaDetailView,
                            agendaId = args.agendaId
                        )
                    ) {
                        inclusive = true
                    }
                }
            },
            onNavigateToEdit = {
                navController.navigate(
                    AgendaDetailScreen(
                        agendaItemType = args.agendaItemType,
                        agendaDetailView = AgendaDetailView.EDIT,
                        agendaId = args.agendaId
                    )
                )
            },
            onNavigateToEditText = { fieldType, text ->
                navController.navigateToAgendaEditTextScreen(
                    editTextFieldType = fieldType,
                    text = text
                )
            },
            onNavigateToPhotoDetail = { photoId, photoUri ->
                navController.navigateToAgendaPhotoDetailScreen(
                    photoId = photoId, photoUri = photoUri
                )
            }
        )
    }
}

fun NavController.navigateToAgendaEditTextScreen(editTextFieldType: AgendaEditTextFieldType,
                                                 text: String)
= navigate(AgendaEditTextScreen(
    editTextFieldType = editTextFieldType,
    text = text
))

fun NavGraphBuilder.agendaEditTextScreen(
    navController: NavController
) {
    composable<AgendaEditTextScreen> {
        val args = it.toRoute<AgendaEditTextScreen>()
        AgendaEditTextScreenRoot(
            fieldType = args.editTextFieldType,
            text = args.text,
            onCancelClick = {
                navController.popBackStack()
            },
            onSaveClick = { text ->
                navController.previousBackStackEntry
                    ?.savedStateHandle?.apply {
                        set("text", text.trim())
                        set("fieldType", args.editTextFieldType)
                    }
                navController.navigateUp()
            },
        )
    }
}

fun NavController.navigateToAgendaPhotoDetailScreen(photoId: String, photoUri: String)
        = navigate(AgendaPhotoDetailScreen(photoId = photoId, photoUri = photoUri))

fun NavGraphBuilder.agendaPhotoDetailScreen(
    navController: NavController
) {
    composable<AgendaPhotoDetailScreen> {
        val args = it.toRoute<AgendaPhotoDetailScreen>()
        PhotoDetailScreenRoot(
            imageUri = args.photoUri,
            onCloseClick = {
                navController.navigateUp()
            },
            onDeleteClick = {
                navController.previousBackStackEntry
                    ?.savedStateHandle?.apply {
                        set("photoId", args.photoId)
                        set("photoDetailAction", PhotoDetailAction.DELETE)
                    }
                navController.navigateUp()
            }
        )
    }
}

@Serializable
object AgendaGraph

@Serializable
object AgendaListScreen

@Serializable
data class AgendaDetailScreen(
    val agendaItemType: AgendaItemType,
    val agendaDetailView: AgendaDetailView,
    val agendaId: String = ""
)

@Serializable
data class AgendaEditTextScreen(
    val editTextFieldType: AgendaEditTextFieldType,
    val text: String
)

@Serializable
data class AgendaPhotoDetailScreen(
    val photoId: String,
    val photoUri: String
)
