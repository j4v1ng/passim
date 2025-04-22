package com.passim.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import com.passim.model.Records
import com.passim.model.Managers
import java.security.MessageDigest

/**
 * Hash a password using SHA-256
 */
private fun hashPassword(password: String): String {
    return MessageDigest.getInstance("SHA-256")
        .digest(password.toByteArray())
        .fold("") { str, it -> str + "%02x".format(it) }
}

fun Application.configureDatabase() {
    // Connect to the database
    Database.connect(
        url = environment.config.property("database.url").getString(),
        driver = environment.config.property("database.driver").getString(),
        user = environment.config.propertyOrNull("database.user")?.getString() ?: "",
        password = environment.config.propertyOrNull("database.password")?.getString() ?: ""
    )

    // Create tables if they don't exist
    transaction {
        SchemaUtils.create(Records, Managers)

        // Insert admin user if it doesn't exist
        if (Managers.selectAll().count() == 0L) {
            // Hash the password
            val hashedPassword = hashPassword("admin")

            Managers.insert {
                it[password] = hashedPassword
            }
        }
    }
}
