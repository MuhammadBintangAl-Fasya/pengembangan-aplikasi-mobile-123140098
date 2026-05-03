package com.bintang.myprofileapp.ai.model

import io.ktor.client.plugins.*
import kotlinx.coroutines.delay
import kotlinx.serialization.SerializationException

sealed class AIError : Exception() {
    data class Unauthorized(override val message: String = "API key tidak valid") : AIError()
    data class RateLimited(
        val retryAfter: Int = 60,
        override val message: String = "Terlalu banyak permintaan. Coba lagi dalam $retryAfter detik"
    ) : AIError()
    data class ServerError(override val message: String = "Server AI sedang bermasalah") : AIError()
    data class NetworkError(override val message: String = "Tidak ada koneksi internet") : AIError()
    data class ParseError(override val message: String = "Gagal memproses respons AI") : AIError()
    data class Unknown(override val message: String = "Terjadi kesalahan yang tidak diketahui") : AIError()
}

suspend fun <T> safeAICall(block: suspend () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: ClientRequestException) {
        when (e.response.status.value) {
            401 -> Result.failure(AIError.Unauthorized())
            403 -> Result.failure(AIError.Unauthorized("API key tidak memiliki akses"))
            429 -> {
                val retryAfter = e.response.headers["Retry-After"]?.toIntOrNull() ?: 60
                Result.failure(AIError.RateLimited(retryAfter))
            }
            in 500..599 -> Result.failure(AIError.ServerError())
            else -> Result.failure(AIError.Unknown("HTTP Error: ${e.response.status.value}"))
        }
    } catch (e: ServerResponseException) {
        Result.failure(AIError.ServerError("Server error: ${e.response.status.value}"))
    } catch (e: SerializationException) {
        Result.failure(AIError.ParseError())
    } catch (e: Exception) {
        if (e::class.simpleName?.contains("IO") == true ||
            e::class.simpleName?.contains("Connect") == true ||
            e::class.simpleName?.contains("Timeout") == true) {
            Result.failure(AIError.NetworkError())
        } else {
            Result.failure(AIError.Unknown(e.message ?: "Unknown error"))
        }
    }
}

suspend fun <T> retryWithBackoff(
    times: Int = 3,
    initialDelay: Long = 1000,
    maxDelay: Long = 10000,
    factor: Double = 2.0,
    block: suspend () -> T
): T {
    var currentDelay = initialDelay
    repeat(times - 1) {
        try {
            return block()
        } catch (e: Exception) {
            when {
                e is AIError.RateLimited -> {
                    delay(e.retryAfter * 1000L)
                }
                e is AIError.ServerError -> {
                    delay(currentDelay)
                    currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
                }
                else -> throw e
            }
        }
    }
    return block()
}
