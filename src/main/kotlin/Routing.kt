package com.example

import com.example.routes.apiRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("LABSBACKEND conectado a MySQL")
        }
        apiRoutes()
    }
}