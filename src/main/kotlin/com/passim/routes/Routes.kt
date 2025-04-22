package com.passim.routes

import com.passim.UserSession
import com.passim.service.AdminService
import com.passim.service.CaptchaService
import com.passim.service.LinkService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

/**
 * Configure routes for the home page and URL submission
 */
fun Route.configureHomeRoutes(linkService: LinkService) {
    // Home page
    get("/") {
        call.respondText(
            """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Passim - Random URL Generator</title>
                <link rel="stylesheet" href="/static/css/styles.css">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
                <link rel="icon" href="/static/images/favicon.png">
            </head>
            <body>
                <nav class="navbar">
                    <div class="brand-container">
                        <a href="/" class="navbar-brand">Passim</a>
                        <span class="brand-tagline">here, there, everywhere</span>
                    </div>
                    <div class="navbar-nav">
                        <a href="/" class="nav-link"><i class="fas fa-home"></i> Home</a>
                        <a href="/new" class="nav-link"><i class="fas fa-plus-circle"></i> Add Link</a>
                        <a href="/manage" class="nav-link"><i class="fas fa-cog"></i> Admin</a>
                    </div>
                </nav>

                <div class="main-container">
                    <div class="hero">
                        <h1>Discover Something New</h1>
                        <p><em>Passim</em> — Latin for "here, there, everywhere" — randomly takes you to interesting websites submitted by users around the world. Click the button below to start your journey or add your own link to the collection.</p>
                    </div>

                    <div class="container">
                        <div class="left-container">
                            <form action="/random" method="get" target="_blank">
                                <button type="submit" class="random-button">
                                    <i class="fas fa-random fa-3x mb-2"></i>
                                    <span>Go to Random Link</span>
                                </button>
                            </form>
                        </div>
                        <div class="right-container">
                            <div class="card">
                                <div class="card-header">
                                    Welcome to Passim! <small>here, there, everywhere</small>
                                </div>
                                <div class="card-body">
                                    <p class="welcome-text">
                                        <em>Passim</em> — Latin for "here, there, everywhere" — is a simple tool that helps you discover new websites across the internet.
                                        <br><br>
                                        Click the button to go to a random website from our collection and explore the web: here, there, and everywhere!
                                        <br><br>
                                        Have a cool website to share? Add it to our collection!
                                    </p>
                                    <a href="/new" class="btn btn-secondary">
                                        <i class="fas fa-plus-circle"></i> Add a new link
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <footer class="footer">
                    <p>&copy; 2023 Passim - Random URL Generator | <em>here, there, everywhere</em></p>
                </footer>
            </body>
            </html>
            """.trimIndent(),
            ContentType.Text.Html
        )
    }

    // Random URL redirect
    get("/random") {
        val randomUrl = linkService.getRandomUrl()
        if (randomUrl != null) {
            // Check if the URL already has a protocol
            val redirectUrl = if (randomUrl.startsWith("http://") || randomUrl.startsWith("https://")) {
                randomUrl
            } else {
                "http://$randomUrl"
            }
            call.respondRedirect(redirectUrl)
        } else {
            call.respondRedirect("/")
        }
    }

    // New link submission form
    get("/new") {
        call.respondText(
            """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Passim - Add New Link</title>
                <link rel="stylesheet" href="/static/css/styles.css">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
                <link rel="icon" href="/static/images/favicon.png">
            </head>
            <body>
                <nav class="navbar">
                    <div class="brand-container">
                        <a href="/" class="navbar-brand">Passim</a>
                        <span class="brand-tagline">here, there, everywhere</span>
                    </div>
                    <div class="navbar-nav">
                        <a href="/" class="nav-link"><i class="fas fa-home"></i> Home</a>
                        <a href="/new" class="nav-link"><i class="fas fa-plus-circle"></i> Add Link</a>
                        <a href="/manage" class="nav-link"><i class="fas fa-cog"></i> Admin</a>
                    </div>
                </nav>

                <div class="main-container">
                    <div class="hero">
                        <h1>Add a New Link</h1>
                        <p>Share an interesting website with the Passim community.</p>
                    </div>

                    <div class="form-container">
                        <div class="card">
                            <div class="card-header">
                                <i class="fas fa-link"></i> Submit a New URL
                            </div>
                            <div class="card-body">
                                <form action="/submit" method="post">
                                    <div class="form-group">
                                        <label for="url">Website URL:</label>
                                        <input type="text" id="url" name="url" class="form-control" placeholder="e.g., example.com" required>
                                        <small class="text-muted">Please enter the URL without http:// or https://</small>
                                    </div>
                                    <div class="form-group">
                                        <button type="submit" class="submit-button">
                                            <i class="fas fa-paper-plane"></i> Submit Link
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>

                        <div class="text-center mt-3">
                            <a href="/" class="go-back-link">
                                <i class="fas fa-arrow-left"></i> Back to Home
                            </a>
                        </div>
                    </div>
                </div>

                <footer class="footer">
                    <p>&copy; 2023 Passim - Random URL Generator | <em>here, there, everywhere</em></p>
                </footer>
            </body>
            </html>
            """.trimIndent(),
            ContentType.Text.Html
        )
    }

    // Submit new link
    post("/submit") {
        val parameters = call.receiveParameters()
        val url = parameters["url"] ?: ""

        // Validate URL
        if (url.isBlank()) {
            call.respondRedirect("/new?error=url")
            return@post
        }

        // Check if URL already exists
        if (linkService.urlExists(url)) {
            call.respondRedirect("/new?error=duplicate")
            return@post
        }

        // Submit URL
        linkService.submitUrl(url)
        call.respondRedirect("/?success=true")
    }
}

/**
 * Configure routes for admin functionality
 */
fun Route.configureAdminRoutes(adminService: AdminService, linkService: LinkService) {
    // Admin login page
    get("/manage") {
        val session = call.sessions.get<UserSession>()
        if (session != null && session.isAdmin) {
            // Admin is logged in, show management page
            val records = linkService.getAllRecords()
            call.respondText(
                """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Passim - Admin Dashboard</title>
                    <link rel="stylesheet" href="/static/css/styles.css">
                    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
                    <link rel="icon" href="/static/images/favicon.png">
                </head>
                <body>
                    <nav class="navbar">
                        <div class="brand-container">
                            <a href="/" class="navbar-brand">Passim</a>
                            <span class="brand-tagline">here, there, everywhere</span>
                        </div>
                        <div class="navbar-nav">
                            <a href="/" class="nav-link"><i class="fas fa-home"></i> Home</a>
                            <a href="/new" class="nav-link"><i class="fas fa-plus-circle"></i> Add Link</a>
                            <a href="/manage" class="nav-link"><i class="fas fa-cog"></i> Admin</a>
                        </div>
                    </nav>

                    <div class="main-container">
                        <div class="hero">
                            <h1>Admin Dashboard</h1>
                            <p>Manage all submitted links in the Passim database.</p>
                        </div>

                        <div class="card mb-4">
                            <div class="card-header">
                                <i class="fas fa-link"></i> Submitted Links
                            </div>
                            <div class="card-body">
                                <div class="table-container">
                                    <table>
                                        <thead>
                                            <tr>
                                                <th>URL</th>
                                                <th>Submission Date</th>
                                                <th>Status</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            ${records.joinToString("") { record ->
                                                """
                                                <tr>
                                                    <td><a href="${if (record.url.startsWith("http://") || record.url.startsWith("https://")) record.url else "http://${record.url}"}" target="_blank">${record.url}</a></td>
                                                    <td>${record.submissionDate}</td>
                                                    <td>${if (record.unacceptableContent) "<span class='badge badge-danger'>Flagged</span>" else "<span class='badge badge-success'>Active</span>"}</td>
                                                    <td>
                                                        <a href="/delete/${record.id}" class="action-link" onclick="return confirm('Are you sure you want to delete this link?')">
                                                            <i class="fas fa-trash"></i> Delete
                                                        </a>
                                                    </td>
                                                </tr>
                                                """.trimIndent()
                                            }}
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>

                        <div class="text-center mb-4">
                            <form action="/logout" method="post">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-sign-out-alt"></i> Logout
                                </button>
                            </form>
                        </div>
                    </div>

                    <footer class="footer">
                        <p>&copy; 2023 Passim - Random URL Generator | <em>here, there, everywhere</em></p>
                    </footer>
                </body>
                </html>
                """.trimIndent(),
                ContentType.Text.Html
            )
        } else {
            // Admin is not logged in, show login form
            call.respondText(
                """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Passim - Admin Login</title>
                    <link rel="stylesheet" href="/static/css/styles.css">
                    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
                    <link rel="icon" href="/static/images/favicon.png">
                </head>
                <body>
                    <nav class="navbar">
                        <div class="brand-container">
                            <a href="/" class="navbar-brand">Passim</a>
                            <span class="brand-tagline">here, there, everywhere</span>
                        </div>
                        <div class="navbar-nav">
                            <a href="/" class="nav-link"><i class="fas fa-home"></i> Home</a>
                            <a href="/new" class="nav-link"><i class="fas fa-plus-circle"></i> Add Link</a>
                            <a href="/manage" class="nav-link"><i class="fas fa-cog"></i> Admin</a>
                        </div>
                    </nav>

                    <div class="main-container">
                        <div class="hero">
                            <h1>Admin Access Required</h1>
                            <p>Please enter your password to access the admin dashboard.</p>
                        </div>

                        <div class="form-container">
                            <div class="card">
                                <div class="card-header">
                                    <i class="fas fa-lock"></i> Admin Login
                                </div>
                                <div class="card-body">
                                    <form action="/login" method="post">
                                        <div class="form-group">
                                            <label for="password">Password:</label>
                                            <input type="password" id="password" name="password" class="form-control" required>
                                        </div>
                                        <div class="form-group">
                                            <button type="submit" class="submit-button">
                                                <i class="fas fa-sign-in-alt"></i> Login
                                            </button>
                                        </div>
                                    </form>
                                </div>
                            </div>

                            <div class="text-center mt-3">
                                <a href="/" class="go-back-link">
                                    <i class="fas fa-arrow-left"></i> Back to Home
                                </a>
                            </div>
                        </div>
                    </div>

                    <footer class="footer">
                        <p>&copy; 2023 Passim - Random URL Generator | <em>here, there, everywhere</em></p>
                    </footer>
                </body>
                </html>
                """.trimIndent(),
                ContentType.Text.Html
            )
        }
    }

    // Admin login
    post("/login") {
        val parameters = call.receiveParameters()
        val password = parameters["password"] ?: ""

        if (adminService.verifyPassword(password)) {
            call.sessions.set(UserSession(isAdmin = true))
            call.respondRedirect("/manage")
        } else {
            call.respondRedirect("/")
        }
    }

    // Admin logout
    post("/logout") {
        call.sessions.clear<UserSession>()
        call.respondRedirect("/")
    }

    // Delete record
    get("/delete/{id}") {
        val session = call.sessions.get<UserSession>()
        if (session != null && session.isAdmin) {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id != null) {
                adminService.deleteRecord(id)
            }
            call.respondRedirect("/manage")
        } else {
            call.respondRedirect("/")
        }
    }
}

/**
 * Configure routes for captcha functionality
 */
fun Route.configureCaptchaRoutes(captchaService: CaptchaService) {
    // Generate captcha ID
    get("/captcha-id") {
        val (sessionId, _) = captchaService.generateCaptcha()
        call.respondText(sessionId)
    }

    // Generate captcha image
    get("/captcha") {
        val (sessionId, base64Image) = captchaService.generateCaptcha()
        call.respondText(
            """
            <img src="data:image/png;base64,$base64Image" alt="CAPTCHA">
            <input type="hidden" id="captcha-id" name="captchaId" value="$sessionId">
            """.trimIndent(),
            ContentType.Text.Html
        )
    }

    // Validate captcha
    post("/validate-captcha") {
        val parameters = call.receiveParameters()
        val sessionId = parameters["sessionId"] ?: ""
        val answer = parameters["answer"] ?: ""

        val isValid = captchaService.validateCaptcha(sessionId, answer)
        call.respond(mapOf("valid" to isValid))
    }
}
