package com.bintang.newsreader.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bintang.newsreader.data.Article
import com.bintang.newsreader.data.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    // State untuk daftar artikel
    private val _articlesState = MutableStateFlow<UiState<List<Article>>>(UiState.Loading)
    val articlesState: StateFlow<UiState<List<Article>>> = _articlesState.asStateFlow()

    // State untuk pull-to-refresh (isRefreshing)
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        loadArticles()
    }

    // Load artikel dari repository
    fun loadArticles() {
        viewModelScope.launch {
            _articlesState.value = UiState.Loading
            repository.getArticles()
                .onSuccess { articles ->
                    _articlesState.value = UiState.Success(articles)
                }
                .onFailure { error ->
                    _articlesState.value = UiState.Error(
                        error.message ?: "Terjadi kesalahan yang tidak diketahui"
                    )
                }
        }
    }

    // Pull-to-refresh: reload tanpa mengubah state ke Loading
    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            repository.getArticles()
                .onSuccess { articles ->
                    _articlesState.value = UiState.Success(articles)
                }
                .onFailure { error ->
                    _articlesState.value = UiState.Error(
                        error.message ?: "Gagal memuat ulang data"
                    )
                }
            _isRefreshing.value = false
        }
    }
}
