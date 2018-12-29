package com.freesundance

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main(args: Array<String>) {
    val jsonResponse = """{
    "id": 1,
    "task": "Pay waterbill",
    "description": "Pay water bill today"
}"""

    embeddedServer(Netty, 8080) {
        install(Routing) {
            get("/todo") {
                call.respondText(jsonResponse, ContentType.Application.Json)
            }
        }
    }.start(wait = true)
}

