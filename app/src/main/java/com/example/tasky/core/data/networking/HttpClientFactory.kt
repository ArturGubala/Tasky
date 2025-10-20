package com.example.tasky.core.data.networking

import com.example.tasky.BuildConfig
import com.example.tasky.core.domain.datastore.AuthInfo
import com.example.tasky.core.domain.datastore.SessionStorage
import com.example.tasky.core.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber

class HttpClientFactory(
    private val sessionStorage: SessionStorage
) {

    fun build(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                        explicitNulls = true
                    }
                )
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.d(message)
                    }
                }
                level = LogLevel.ALL
            }
            install(createClientPlugin("ConditionalHeaders") {
                onRequest { request, _ ->
                    val isS3 = request.url.host.contains("amazonaws.com")

                    // DO NOT SEND HEADERS WHEN UPLOAD PHOTOS
                    if (!isS3) {
                        request.contentType(ContentType.Application.Json)
                        request.headers.append("x-api-key", BuildConfig.API_KEY)
                        request.headers.append(HttpHeaders.Accept, "application/json")
                        request.headers.append(HttpHeaders.AcceptCharset, "UTF-8")
                    }
                }
            })
            install(Auth) {
                bearer {
                    loadTokens {
                        sessionStorage.get()?.let { info ->
                            BearerTokens(
                                accessToken = info.accessToken,
                                refreshToken = info.refreshToken
                            )
                        }
                    }
                    refreshTokens {
                        val info = sessionStorage.get()
                        val response = client.post<AccessTokenRequest, AccessTokenResponse>(
                            route = "/auth/refresh",
                            body = AccessTokenRequest(
                                refreshToken = info?.refreshToken ?: ""
                            )
                        )

                        if(response is Result.Success) {
                            val newAuthInfo = AuthInfo(
                                accessToken = response.data.accessToken,
                                refreshToken = response.data.refreshToken,
                                userName = info?.userName ?: "",
                                userId = info?.userId ?: ""
                            )
                            sessionStorage.set(newAuthInfo)

                            BearerTokens(
                                accessToken = newAuthInfo.accessToken,
                                refreshToken = newAuthInfo.refreshToken
                            )
                        } else {
                            BearerTokens(
                                accessToken = "",
                                refreshToken = ""
                            )
                        }
                    }
                    // DO NOT SEND BEARER WHEN UPLOAD PHOTOS
                    sendWithoutRequest { request ->
                        !request.url.host.contains("amazonaws.com")
                    }
                }
            }
        }
    }
}
