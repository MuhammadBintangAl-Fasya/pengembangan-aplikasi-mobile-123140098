package com.bintang.newsreader

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform