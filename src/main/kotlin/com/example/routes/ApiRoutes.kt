package com.example.routes

import com.example.database.DatabaseFactory
import com.example.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.apiRoutes() {
    // --- LOGIN Y USUARIOS ---
    post("/login") {
        val req = call.receive<LoginRequest>()
        var usuarioEncontrado: Usuario? = null

        DatabaseFactory.getConnection().use { conn ->
            val stmt = conn.prepareStatement(
                "SELECT id_usuario, nombre, correo, rol FROM usuarios WHERE correo = ? AND password = ?"
            )
            stmt.setString(1, req.correo)
            stmt.setString(2, req.password)
            val rs = stmt.executeQuery()
            if (rs.next()) {
                usuarioEncontrado = Usuario(
                    idUsuario = rs.getInt("id_usuario"),
                    nombre = rs.getString("nombre"),
                    correo = rs.getString("correo"),
                    rol = rs.getString("rol")
                )
            }
        }

        if (usuarioEncontrado != null) {
            call.respond(HttpStatusCode.OK, usuarioEncontrado!!)
        } else {
            call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Credenciales incorrectas"))
        }
    }

    post("/usuarios") {
        val u = call.receive<Usuario>()
        DatabaseFactory.getConnection().use { conn ->
            val stmt = conn.prepareStatement(
                "INSERT INTO usuarios (nombre, correo, password, rol) VALUES (?, ?, ?, ?)"
            )
            stmt.setString(1, u.nombre)
            stmt.setString(2, u.correo)
            stmt.setString(3, u.password ?: "123456")
            stmt.setString(4, u.rol)
            stmt.executeUpdate()
        }
        call.respond(HttpStatusCode.Created, mapOf("mensaje" to "Usuario registrado"))
    }

    // --- EDIFICIOS ---
    get("/edificios") {
        val lista = mutableListOf<Edificio>()
        DatabaseFactory.getConnection().use { conn ->
            val rs = conn.prepareStatement("SELECT id_edificio, nombre, descripcion FROM edificios").executeQuery()
            while (rs.next()) {
                lista.add(
                    Edificio(
                        idEdificio = rs.getInt("id_edificio"),
                        nombre = rs.getString("nombre"),
                        descripcion = rs.getString("descripcion")
                    )
                )
            }
        }
        call.respond(lista)
    }

    // --- ESPACIOS (LABORATORIOS / AULAS) ---
    get("/espacios") {
        val lista = mutableListOf<Espacio>()
        DatabaseFactory.getConnection().use { conn ->
            val rs = conn.prepareStatement("SELECT id_espacio, id_edificio, codigo FROM espacios").executeQuery()
            while (rs.next()) {
                lista.add(
                    Espacio(
                        idEspacio = rs.getInt("id_espacio"),
                        idEdificio = rs.getInt("id_edificio"),
                        codigo = rs.getString("codigo")
                    )
                )
            }
        }
        call.respond(lista)
    }

    post("/espacios") {
        val nuevo = call.receive<Espacio>()
        DatabaseFactory.getConnection().use { conn ->
            val stmt = conn.prepareStatement("INSERT INTO espacios (id_edificio, codigo) VALUES (?, ?)")
            stmt.setInt(1, nuevo.idEdificio)
            stmt.setString(2, nuevo.codigo)
            stmt.executeUpdate()
        }
        call.respond(HttpStatusCode.Created, mapOf("mensaje" to "Espacio creado"))
    }

    // --- RESERVACIONES ---
    get("/reservaciones") {
        val lista = mutableListOf<Reservacion>()
        DatabaseFactory.getConnection().use { conn ->
            val rs = conn.prepareStatement(
                "SELECT id_reservacion, id_usuario, id_espacio, fecha, hora_inicio, hora_fin, motivo, estado FROM reservaciones"
            ).executeQuery()
            while (rs.next()) {
                lista.add(
                    Reservacion(
                        idReservacion = rs.getInt("id_reservacion"),
                        idUsuario = rs.getInt("id_usuario"),
                        idEspacio = rs.getInt("id_espacio"),
                        fecha = rs.getString("fecha"),
                        horaInicio = rs.getString("hora_inicio"),
                        horaFin = rs.getString("hora_fin"),
                        motivo = rs.getString("motivo"),
                        estado = rs.getString("estado")
                    )
                )
            }
        }
        call.respond(lista)
    }

    post("/reservaciones") {
        val r = call.receive<Reservacion>()
        DatabaseFactory.getConnection().use { conn ->
            val stmt = conn.prepareStatement(
                "INSERT INTO reservaciones (id_usuario, id_espacio, fecha, hora_inicio, hora_fin, motivo, estado) VALUES (?, ?, ?, ?, ?, ?, ?)"
            )
            stmt.setInt(1, r.idUsuario)
            stmt.setInt(2, r.idEspacio)
            stmt.setString(3, r.fecha)
            stmt.setString(4, r.horaInicio)
            stmt.setString(5, r.horaFin)
            stmt.setString(6, r.motivo ?: "Práctica de laboratorio")
            stmt.setString(7, r.estado ?: "Confirmada")
            stmt.executeUpdate()
        }
        call.respond(HttpStatusCode.Created, mapOf("mensaje" to "Reservación guardada"))
    }
}