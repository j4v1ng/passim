package com.passim.service

import com.passim.model.Record
import com.passim.repository.ManagerRepository
import com.passim.repository.RecordRepository
import java.awt.Color
import java.awt.Font
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.imageio.ImageIO

/**
 * Service for managing links
 */
class LinkService(private val recordRepository: RecordRepository) {
    /**
     * Get a random URL from the database
     */
    fun getRandomUrl(): String? {
        return recordRepository.getRandomRecord()?.url
    }

    /**
     * Submit a new URL to the database
     */
    fun submitUrl(url: String): Long {
        // Clean the URL (remove http:// or https:// if present)
        val cleanUrl = when {
            url.startsWith("http://") -> url.substring(7)
            url.startsWith("https://") -> url.substring(8)
            else -> url
        }

        // Create a new record
        val record = Record(
            url = cleanUrl,
            submissionDate = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date()),
            unacceptableContent = false
        )

        // Insert the record
        return recordRepository.insertRecord(record)
    }

    /**
     * Check if a URL already exists in the database
     */
    fun urlExists(url: String): Boolean {
        return recordRepository.urlExists(url)
    }

    /**
     * Get all records from the database
     */
    fun getAllRecords(): List<Record> {
        return recordRepository.getAllRecords()
    }
}

/**
 * Service for admin operations
 */
class AdminService(
    private val managerRepository: ManagerRepository,
    private val recordRepository: RecordRepository
) {
    /**
     * Verify admin password
     */
    fun verifyPassword(password: String): Boolean {
        val storedHash = managerRepository.getAdminPassword()
        return storedHash != null && verifyPassword(password, storedHash)
    }

    /**
     * Delete a record
     */
    fun deleteRecord(id: Long): Boolean {
        return recordRepository.deleteRecord(id)
    }

    /**
     * Hash a password
     */
    private fun hashPassword(password: String): String {
        return java.security.MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())
            .fold("") { str, it -> str + "%02x".format(it) }
    }

    /**
     * Verify a password against a hash
     */
    private fun verifyPassword(password: String, storedHash: String): Boolean {
        return hashPassword(password) == storedHash
    }
}

/**
 * Service for captcha generation and validation
 */
class CaptchaService {
    private val captchaMap = mutableMapOf<String, String>()
    private val random = java.util.Random()
    private val chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789"

    /**
     * Generate a new captcha
     */
    fun generateCaptcha(): Pair<String, String> {
        // Generate random text
        val captchaText = (1..6).map { chars[random.nextInt(chars.length)] }.joinToString("")

        // Create image
        val width = 200
        val height = 50
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val graphics = image.createGraphics()

        // Set background
        graphics.color = Color.WHITE
        graphics.fillRect(0, 0, width, height)

        // Draw noise (random lines)
        graphics.color = Color.LIGHT_GRAY
        for (i in 0 until 20) {
            val x1 = random.nextInt(width)
            val y1 = random.nextInt(height)
            val x2 = random.nextInt(width)
            val y2 = random.nextInt(height)
            graphics.drawLine(x1, y1, x2, y2)
        }

        // Draw text
        graphics.color = Color.BLACK
        graphics.font = Font("Arial", Font.BOLD, 30)
        val fontMetrics = graphics.fontMetrics
        val x = (width - fontMetrics.stringWidth(captchaText)) / 2
        val y = (height - fontMetrics.height) / 2 + fontMetrics.ascent
        graphics.drawString(captchaText, x, y)

        graphics.dispose()

        // Convert to base64
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(image, "png", outputStream)
        val base64Image = Base64.getEncoder().encodeToString(outputStream.toByteArray())

        // Store in map
        val sessionId = java.util.UUID.randomUUID().toString()
        captchaMap[sessionId] = captchaText

        return Pair(sessionId, base64Image)
    }

    /**
     * Validate a captcha
     */
    fun validateCaptcha(sessionId: String, answer: String): Boolean {
        val correctAnswer = captchaMap[sessionId]
        if (correctAnswer != null && correctAnswer.equals(answer, ignoreCase = true)) {
            captchaMap.remove(sessionId)
            return true
        }
        return false
    }
}
