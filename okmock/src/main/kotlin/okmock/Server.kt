/*
 * Copyright (c) 2018, OKMock.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package okmock

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.websocket.webSocket

/**
 * @author Saeed Masoumi (7masoumi@gmail.com)
 */

class Server(private val config: Config) {

    private lateinit var engine: NettyApplicationEngine

    fun start() {
        engine = embeddedServer(Netty, config.port, module = Application::okmockServer).start(true)
    }

    fun stop() {
        //engine.stop()
    }

}

fun Application.okmockServer() {
    routing {
        get("/") {
            call.respondText("Hello World!", ContentType.Text.Plain)
        }
        webSocket {

        }
    }

}

