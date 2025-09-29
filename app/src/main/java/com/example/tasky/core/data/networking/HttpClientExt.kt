package com.example.tasky.core.data.networking

import com.example.tasky.BuildConfig
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.ErrorResponseDto
import com.example.tasky.core.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException

suspend inline fun <reified Response: Any> HttpClient.get(
    route: String,
    queryParameters: Map<String, Any?> = mapOf()
): Result<Response, DataError.Network> {
    return safeCall {
        get {
            url(constructRoute(route))
            queryParameters.forEach { (key, value) ->
                parameter(key, value)
            }
        }
    }
}

suspend inline fun <reified Request, reified Response: Any> HttpClient.post(
    route: String,
    body: Request
): Result<Response, DataError.Network> {
    return safeCall {
        post {
            url(constructRoute(route))
            setBody(body)
        }
    }
}

suspend inline fun <reified Request, reified Response : Any> HttpClient.put(
    route: String,
    body: Request,
): Result<Response, DataError.Network> {
    return safeCall {
        put {
            url(constructRoute(route))
            setBody(body)
        }
    }
}

suspend inline fun <reified Response: Any> HttpClient.delete(
    route: String,
    queryParameters: Map<String, Any?> = mapOf()
): Result<Response, DataError.Network> {
    return safeCall {
        delete {
            url(constructRoute(route))
            queryParameters.forEach { (key, value) ->
                parameter(key, value)
            }
        }
    }
}

suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): Result<T, DataError.Network> {
    val response = try {
        execute()
    } catch(e: UnresolvedAddressException) {
        e.printStackTrace()
        return Result.Error(DataError.Network.NoInternet)
    } catch (e: SerializationException) {
        e.printStackTrace()
        return Result.Error(DataError.Network.Serialization)
    } catch(e: Exception) {
        if(e is CancellationException) throw e
        e.printStackTrace()
        return Result.Error(DataError.Network.Unknown)
    }

    return responseToResult(response)
}

suspend inline fun <reified T> responseToResult(response: HttpResponse): Result<T, DataError.Network> {
    return when(response.status.value) {
        in 200..299 -> Result.Success(response.body<T>())
        else -> {
            val errorMessage = try {
                response.body<ErrorResponseDto>().reason.firstOrNull()
            } catch (_: Exception) {
                null
            }

            val error = when (response.status.value) {
                400 -> DataError.Network.BadRequest(errorMessage)
                401 -> DataError.Network.Unauthorized
                403 -> DataError.Network.Forbidden
                404 -> DataError.Network.NotFound(errorMessage)
                409 -> DataError.Network.Conflict(errorMessage)
                429 -> DataError.Network.TooManyRequests
                in 500..599 -> DataError.Network.ServerError
                else -> DataError.Network.Unknown
            }

            Result.Error(error)
        }
    }
}

fun constructRoute(route: String): String {
    return when {
        route.contains(BuildConfig.BASE_URL) -> route
        route.startsWith("/") -> BuildConfig.BASE_URL + route
        else -> BuildConfig.BASE_URL + "/$route"
    }
}
