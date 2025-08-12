package com.example.tasky.auth.data

import com.example.tasky.auth.domain.AuthRepository
import com.example.tasky.core.data.networking.post
import com.example.tasky.core.domain.datastore.AuthInfo
import com.example.tasky.core.domain.datastore.SessionStorage
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult
import com.example.tasky.core.domain.util.Result
import com.example.tasky.core.domain.util.asEmptyDataResult
import io.ktor.client.HttpClient

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
): AuthRepository {

    override suspend fun login(email: String, password: String): EmptyResult<DataError.Network> {
        val result = httpClient.post<LoginRequest, LoginResponse>(
            route = "/auth/login",
            body = LoginRequest(
                email = email,
                password = password
            )
        )
        if(result is Result.Success) {
            sessionStorage.set(
                AuthInfo(
                    accessToken = result.data.accessToken,
                    refreshToken = result.data.refreshToken,
                    userName = result.data.username,
                    userId = result.data.userId
                )
            )
        }
        return result.asEmptyDataResult()
    }

    override suspend fun register(
        fullName: String, email: String, password: String): EmptyResult<DataError.Network> {
        return httpClient.post<RegisterRequest, Unit>(
            route = "/auth/register",
            body = RegisterRequest(
                fullName = fullName,
                email = email,
                password = password
            )
        )
    }
}
