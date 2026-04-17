package com.bintang.newsreader.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bintang.newsreader.data.HttpClientFactory
import com.bintang.newsreader.data.NewsApi
import com.bintang.newsreader.data.NewsRepository
import com.bintang.newsreader.ui.NewsViewModel

// Factory untuk membuat ViewModel dengan dependency
object AppModule {
    private val httpClient by lazy { HttpClientFactory.create() }
    private val newsApi by lazy { NewsApi(httpClient) }
    private val newsRepository by lazy { NewsRepository(newsApi) }

    val newsViewModelFactory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            NewsViewModel(newsRepository)
        }
    }
}
