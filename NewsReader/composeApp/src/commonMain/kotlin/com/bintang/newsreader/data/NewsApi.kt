package com.bintang.newsreader.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class NewsApi(private val client: HttpClient) {

    private val baseUrl = "https://jsonplaceholder.typicode.com"

    // GET semua artikel
    suspend fun getArticles(): List<Article> {
        return client.get("$baseUrl/posts").body()
    }

    // GET artikel by ID
    suspend fun getArticleById(id: Int): Article {
        return client.get("$baseUrl/posts/$id").body()
    }
}
