package com.example.tasky.auth.domain

import com.example.tasky.core.domain.PatternValidator

class UserDataValidator(
    private val patternValidator: PatternValidator
) {
    fun isValidName(name: String): Boolean {
        return name.length in ValidationRules.MIN_NAME_LENGTH..ValidationRules.MAX_NAME_LENGTH
    }

    fun validateEmail(email: String): Boolean {
        return patternValidator.matches(email.trim())
    }

    fun validatePassword(password: String): PasswordValidationState {
        val hasMinLength = password.length >= ValidationRules.MIN_PASSWORD_LENGTH
        val hasDigit = password.any { it.isDigit() }
        val hasLowerCaseCharacter = password.any { it.isLowerCase() }
        val hasUpperCaseCharacter = password.any { it.isUpperCase() }

        return PasswordValidationState(
            hasMinLength = hasMinLength,
            hasNumber = hasDigit,
            hasLowerCaseCharacter = hasLowerCaseCharacter,
            hasUpperCaseCharacter = hasUpperCaseCharacter
        )
    }
}
