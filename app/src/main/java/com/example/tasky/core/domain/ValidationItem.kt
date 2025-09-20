package com.example.tasky.core.domain

import com.example.tasky.auth.domain.FocusedField
import com.example.tasky.core.presentation.ui.UiText

data class ValidationItem(
    val message: UiText,
    val isValid: Boolean,
    val focusedField: FocusedField? = null
)
