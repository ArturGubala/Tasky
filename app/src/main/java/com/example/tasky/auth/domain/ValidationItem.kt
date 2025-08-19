package com.example.tasky.auth.domain

import com.example.tasky.core.presentation.ui.UiText

data class ValidationItem(
    val message: UiText,
    val isValid: Boolean,
    val focusedField: FocusedField
)
