package com.example.tasky.auth.presentation.register

import androidx.annotation.StringRes
import com.example.tasky.R
import com.example.tasky.auth.domain.PasswordValidationState
import com.example.tasky.auth.domain.ValidationRules

data class RegisterState (
    val name: String = "",
    val isNameValid: Boolean = false,
    val email: String = "",
    val isEmailValid: Boolean = false,
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val passwordValidationState: PasswordValidationState = PasswordValidationState(),
    val isRegistering: Boolean = false,
    val focusedField: FocusedField? = null,
    val errors: List<ValidationItem> = emptyList()
)

fun RegisterState.getValidationItemsForFocusedField(): List<ValidationItem> {
    return when (focusedField) {
        FocusedField.NAME -> getNameValidationItems()
        FocusedField.EMAIL -> getEmailValidationItems()
        FocusedField.PASSWORD -> passwordValidationState.getPasswordValidationItems()
        null -> emptyList()
    }
}

private fun RegisterState.getNameValidationItems(): List<ValidationItem> {
    return if (name.isNotEmpty() && !isNameValid) {
        listOf(
            ValidationItem(
                textResId = R.string.must_be_a_valid_name,
                isValid = isNameValid,
                formatArgs = listOf(ValidationRules.MIN_NAME_LENGTH, ValidationRules.MAX_NAME_LENGTH),
                focusedField = FocusedField.NAME
            )
        )
    } else emptyList()
}

private fun RegisterState.getEmailValidationItems(): List<ValidationItem> {
    return if (email.isNotEmpty() && !isEmailValid) {
        listOf(
            ValidationItem(
                textResId = R.string.must_be_a_valid_email,
                isValid = isEmailValid,
                focusedField = FocusedField.EMAIL
            )
        )
    } else emptyList()
}
enum class FocusedField {
    NAME, EMAIL, PASSWORD
}

data class ValidationItem(
    @StringRes val textResId: Int,
    val isValid: Boolean,
    val formatArgs: List<Any> = emptyList(),
    val focusedField: FocusedField
)
