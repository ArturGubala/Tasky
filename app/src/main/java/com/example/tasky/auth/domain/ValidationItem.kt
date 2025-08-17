package com.example.tasky.auth.domain

import androidx.annotation.StringRes

data class ValidationItem(
    @param:StringRes val textResId: Int,
    val isValid: Boolean,
    val formatArgs: List<Any> = emptyList(),
    val focusedField: FocusedField
)
