package com.example.tasky.auth.domain

import com.example.tasky.R
import com.example.tasky.auth.presentation.register.FocusedField

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
                    textResId = R.string.at_least_x_characters,
                    isValid = hasMinLength,
                    formatArgs = listOf(ValidationRules.MIN_PASSWORD_LENGTH),
                    focusedField = FocusedField.PASSWORD
                ),
                ValidationItem(
                    textResId = R.string.at_least_one_number,
                    isValid = hasNumber,
                    focusedField = FocusedField.PASSWORD),
                ValidationItem(
                    textResId = R.string.contains_lowercase_char,
                    isValid = hasLowerCaseCharacter,
                    focusedField = FocusedField.PASSWORD),
                ValidationItem(
                    textResId = R.string.contains_uppercase_char,
                    isValid = hasUpperCaseCharacter,
                    focusedField = FocusedField.PASSWORD)
            )
        } else emptyList()
    }
}
