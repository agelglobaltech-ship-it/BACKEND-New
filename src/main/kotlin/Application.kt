package com.example

import com.example.database.DatabaseFactory
import io.ktor.server.application.*

fun Application.module() {
    DatabaseFactory.init()
    configureSerialization()
    configureHTTP()
    configureRouting()
}