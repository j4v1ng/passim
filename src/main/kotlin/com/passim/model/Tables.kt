package com.passim.model

import org.jetbrains.exposed.sql.Table

/**
 * Table for storing URL records
 */
object Records : Table() {
    val id = long("id").autoIncrement()
    val url = varchar("url", 255)
    val submissionDate = varchar("submission_date", 50)
    val unacceptableContent = bool("unacceptable_content").default(false)
    
    override val primaryKey = PrimaryKey(id)
}

/**
 * Table for storing admin accounts
 */
object Managers : Table() {
    val id = long("id").autoIncrement()
    val password = varchar("password", 255)
    
    override val primaryKey = PrimaryKey(id)
}

/**
 * Data class representing a record
 */
data class Record(
    val id: Long = 0,
    val url: String,
    val submissionDate: String,
    val unacceptableContent: Boolean = false
)

/**
 * Data class representing a manager (admin)
 */
data class Manager(
    val id: Long = 0,
    val password: String
)