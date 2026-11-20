package com.example.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection

object DatabaseFactory {
    private lateinit var dataSource: HikariDataSource

    fun init() {
        val dbHost = System.getenv("DB_HOST") ?: "localhost"
        val dbPort = System.getenv("DB_PORT") ?: "3306"
        val dbName = System.getenv("DB_NAME") ?: "encuentra_tu_espacio"
        val dbUser = System.getenv("DB_USER") ?: "root"
        val dbPassword = System.getenv("DB_PASSWORD") ?: "NuevaClave2026!"

        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:mysql://$dbHost:$dbPort/$dbName?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
            driverClassName = "com.mysql.cj.jdbc.Driver"
            username = dbUser
            password = dbPassword
            maximumPoolSize = 5
            isAutoCommit = true
        }

        dataSource = HikariDataSource(config)
    }

    fun getConnection(): Connection = dataSource.connection
}