package com.example.tasky.auth.domain

import androidx.annotation.StringRes
import com.example.tasky.auth.presentation.register.FocusedField

data class ValidationItem(
    @param:StringRes val textResId: Int,
    val isValid: Boolean,
    val formatArgs: List<Any> = emptyList(),
    val focusedField: FocusedField
)
