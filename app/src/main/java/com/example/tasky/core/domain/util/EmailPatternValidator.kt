package com.example.tasky.core.domain.util

import android.util.Patterns
import com.example.tasky.core.domain.PatternValidator

object EmailPatternValidator: PatternValidator {

    override fun matches(value: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }
}
