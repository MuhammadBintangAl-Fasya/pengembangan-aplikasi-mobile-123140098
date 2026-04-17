package com.bintang.newsreader.data

class NewsRepository(private val api: NewsApi) {

    // Mengambil semua artikel, wrapped dalam Result
    suspend fun getArticles(): Result<List<Article>> {
        return try {
            val articles = api.getArticles()
            Result.success(articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Mengambil detail artikel by ID
    suspend fun getArticleById(id: Int): Result<Article> {
        return try {
            val article = api.getArticleById(id)
            Result.success(article)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
