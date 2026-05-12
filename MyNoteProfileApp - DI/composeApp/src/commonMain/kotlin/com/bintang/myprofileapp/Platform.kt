package com.bintang.myprofileapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform