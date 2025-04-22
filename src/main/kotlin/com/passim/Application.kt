package com.passim

import com.passim.di.appModule
import com.passim.plugins.configureDatabase
import com.passim.plugins.configureRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.sessions.*
import kotlinx.serialization.json.Json
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    // Configure plugins
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }

    install(Sessions) {
        cookie<UserSession>("USER_SESSION") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 3600
        }
    }

    install(Koin) {
        modules(appModule)
    }

    // Configure database
    configureDatabase()

    // Configure routing
    configureRouting()
}

data class UserSession(val isAdmin: Boolean = false)
