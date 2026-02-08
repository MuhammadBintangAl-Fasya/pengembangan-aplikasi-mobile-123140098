package com.starproject.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform