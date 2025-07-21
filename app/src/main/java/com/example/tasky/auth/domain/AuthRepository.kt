package com.example.tasky.auth.domain

import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult

interface AuthRepository {
    suspend fun login(email: String, password: String): EmptyResult<DataError.Network>
    suspend fun register(fullName: String, email: String, password: String): EmptyResult<DataError.Network>
}
