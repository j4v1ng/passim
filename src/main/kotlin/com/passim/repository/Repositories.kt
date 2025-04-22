package com.passim.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import com.passim.model.*
import kotlin.random.Random

/**
 * Repository for managing URL records
 */
class RecordRepository {
    /**
     * Insert a new record into the database
     */
    fun insertRecord(record: Record): Long = transaction {
        Records.insert {
            it[url] = record.url
            it[submissionDate] = record.submissionDate
            it[unacceptableContent] = record.unacceptableContent
        }[Records.id]
    }

    /**
     * Get a random record from the database
     */
    fun getRandomRecord(): Record? = transaction {
        val allIds = Records.slice(Records.id).selectAll().map { it[Records.id] }
        if (allIds.isEmpty()) return@transaction null

        val randomId = allIds[Random.nextInt(allIds.size)]
        Records.select { Records.id eq randomId }
            .map { 
                Record(
                    id = it[Records.id],
                    url = it[Records.url],
                    submissionDate = it[Records.submissionDate],
                    unacceptableContent = it[Records.unacceptableContent]
                )
            }
            .singleOrNull()
    }

    /**
     * Check if a URL already exists in the database
     */
    fun urlExists(url: String): Boolean = transaction {
        Records.select { Records.url eq url }.count() > 0
    }

    /**
     * Get all records from the database
     */
    fun getAllRecords(): List<Record> = transaction {
        Records.selectAll().map { 
            Record(
                id = it[Records.id],
                url = it[Records.url],
                submissionDate = it[Records.submissionDate],
                unacceptableContent = it[Records.unacceptableContent]
            )
        }
    }

    /**
     * Delete a record from the database
     */
    fun deleteRecord(id: Long): Boolean = transaction {
        Records.deleteWhere { Records.id eq id } > 0
    }
}

/**
 * Repository for managing admin accounts
 */
class ManagerRepository {
    /**
     * Get the admin password from the database
     */
    fun getAdminPassword(): String? = transaction {
        Managers.slice(Managers.password)
            .selectAll()
            .limit(1)
            .map { it[Managers.password] }
            .singleOrNull()
    }
}
