package com.bintang.newsreader.data

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
) {
    // Gambar dari Picsum dengan seed berdasarkan ID artikel (konsisten)
    val imageUrl: String
        get() = "https://picsum.photos/seed/$id/600/300"

    // Deskripsi singkat (preview di list)
    val shortDescription: String
        get() = if (body.length > 120) body.take(120) + "..." else body

    // Format title agar lebih rapi (capitalize)
    val formattedTitle: String
        get() = title.replaceFirstChar { it.uppercase() }
}
