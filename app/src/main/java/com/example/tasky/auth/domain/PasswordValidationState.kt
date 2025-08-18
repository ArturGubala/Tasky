package com.example.tasky.auth.domain

import com.example.tasky.R
import com.example.tasky.auth.presentation.register.RegisterFocusedField
import com.example.tasky.core.presentation.ui.UiText

data class PasswordValidationState(
    val hasMinLength: Boolean = false,
    val hasNumber: Boolean = false,
    val hasLowerCaseCharacter: Boolean = false,
    val hasUpperCaseCharacter: Boolean = false
) {
    val isValidPassword: Boolean
        get() = hasMinLength && hasNumber && hasLowerCaseCharacter && hasUpperCaseCharacter

    fun getPasswordValidationItems(): List<ValidationItem> {
        return if (!isValidPassword) {
            listOf(
                ValidationItem(
                    message = UiText.StringResource(
                        id = R.string.at_least_x_characters,
                        args = arrayOf(ValidationRules.MIN_PASSWORD_LENGTH)
                    ),
                    isValid = hasMinLength,
                    focusedField = RegisterFocusedField.PASSWORD
                ),
                ValidationItem(
                    message = UiText.StringResource(R.string.at_least_one_number),
                    isValid = hasNumber,
                    focusedField = RegisterFocusedField.PASSWORD),
                ValidationItem(
                    message = UiText.StringResource(R.string.contains_lowercase_char),
                    isValid = hasLowerCaseCharacter,
                    focusedField = RegisterFocusedField.PASSWORD),
                ValidationItem(
                    message = UiText.StringResource(R.string.contains_uppercase_char),
                    isValid = hasUpperCaseCharacter,
                    focusedField = RegisterFocusedField.PASSWORD)
            )
        } else emptyList()
    }
}
