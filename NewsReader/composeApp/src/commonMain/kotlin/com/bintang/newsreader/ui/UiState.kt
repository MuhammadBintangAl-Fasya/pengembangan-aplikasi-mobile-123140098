package com.bintang.newsreader.ui

// Sealed class untuk merepresentasikan semua kemungkinan state UI
sealed class UiState<out T> {
    // State saat data sedang dimuat
    data object Loading : UiState<Nothing>()

    // State saat data berhasil dimuat
    data class Success<T>(val data: T) : UiState<T>()

    // State saat terjadi error
    data class Error(val message: String) : UiState<Nothing>()
}

// Extension function helper
fun <T> UiState<T>.isLoading() = this is UiState.Loading
fun <T> UiState<T>.isSuccess() = this is UiState.Success
fun <T> UiState<T>.isError() = this is UiState.Error
fun <T> UiState<T>.getOrNull(): T? = (this as? UiState.Success)?.data
