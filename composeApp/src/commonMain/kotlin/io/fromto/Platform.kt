package io.fromto

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform