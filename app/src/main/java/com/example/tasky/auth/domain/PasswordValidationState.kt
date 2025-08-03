package com.example.tasky.auth.domain

import com.example.tasky.R
import com.example.tasky.auth.presentation.register.ValidationItem

data class PasswordValidationState(
    val hasMinLength: Boolean = false,
    val hasNumber: Boolean = false,
    val hasLowerCaseCharacter: Boolean = false,
    val hasUpperCaseCharacter: Boolean = false,
    val hasBeenValidated: Boolean = false
) {
    val isValidPassword: Boolean
        get() = hasMinLength && hasNumber && hasLowerCaseCharacter && hasUpperCaseCharacter

    val isValidPasswordOrNull: Boolean?
        get() = if (hasBeenValidated) isValidPassword else null

    fun getPasswordValidationItems(): List<ValidationItem>? {
        return if (!isValidPassword) {
            listOf(
                ValidationItem(
                    textResId = R.string.at_least_x_characters,
                    isValid = hasMinLength,
                    formatArgs = listOf(ValidationRules.MIN_PASSWORD_LENGTH),
                ),
                ValidationItem(R.string.at_least_one_number, hasNumber),
                ValidationItem(R.string.contains_lowercase_char, hasLowerCaseCharacter),
                ValidationItem(R.string.contains_uppercase_char, hasUpperCaseCharacter)
            )
        } else null
    }
}
