package com.passim.plugins

import com.passim.routes.configureAdminRoutes
import com.passim.routes.configureCaptchaRoutes
import com.passim.routes.configureHomeRoutes
import com.passim.service.AdminService
import com.passim.service.CaptchaService
import com.passim.service.LinkService
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val linkService by inject<LinkService>()
    val adminService by inject<AdminService>()
    val captchaService by inject<CaptchaService>()
    
    routing {
        // Serve static files
        static("/static") {
            resources("static")
        }
        
        // Configure routes
        configureHomeRoutes(linkService)
        configureAdminRoutes(adminService, linkService)
        configureCaptchaRoutes(captchaService)
    }
}