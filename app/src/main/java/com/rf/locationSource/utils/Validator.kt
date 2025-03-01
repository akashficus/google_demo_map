package com.rf.locationSource.utils

object Validator {

    private const val USERNAME_MIN_LENGTH = 4
    private const val PASSWORD_MIN_LENGTH = 6
    private val PASSWORD_PATTERN = Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")

    fun validateUsername(username: String): ValidationResult {
        return when {
            username.isEmpty() -> ValidationResult(false, "Username cannot be empty")
            username.length < USERNAME_MIN_LENGTH -> ValidationResult(false, "Username must be at least $USERNAME_MIN_LENGTH characters long")
            else -> ValidationResult(true, "Valid username")
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isEmpty() -> ValidationResult(false, "Password cannot be empty")
            password.length < PASSWORD_MIN_LENGTH -> ValidationResult(false, "Password must be at least $PASSWORD_MIN_LENGTH characters long")
            else -> ValidationResult(true, "Valid password")
        }
    }

    data class ValidationResult(val isValid: Boolean, val message: String)
}