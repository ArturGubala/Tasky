package com.example.tasky.core.domain

interface PatternValidator {
    fun matches(value: String): Boolean
}
