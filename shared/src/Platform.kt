package `in`.antinna.mgtn

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
