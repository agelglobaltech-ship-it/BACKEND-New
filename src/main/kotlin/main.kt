package com.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    val serverPort = System.getenv("SERVER_PORT")?.toIntOrNull() ?: 8080
    embeddedServer(Netty, port = serverPort, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}