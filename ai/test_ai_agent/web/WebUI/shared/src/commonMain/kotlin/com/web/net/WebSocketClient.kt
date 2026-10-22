package com.web.net

import com.web.ui.BuildKonfig
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.utils.io.CancellationException
import io.ktor.websocket.*

class WebSocketClient {

    private val tag = "WebSocketClient"

    private val host: String = BuildKonfig.websocketHost
    private val port: Int = BuildKonfig.websocketPort.toInt()
    private val path: String = ""

    // Configure HttpClient with WebSockets plugin
    private val client = HttpClient {
        install(WebSockets) {
            pingIntervalMillis = 20_000 // Send pings every 20s to keep connection alive
        }
    }

    private var session: DefaultClientWebSocketSession? = null

    suspend fun makeSureConnected() {
        if (session != null) {
            println("$tag:makeSureConnected: already connected")
            return
        }
        try {
            println("$tag:makeSureConnected: connecting...")
            // Open WebSocket connection
            session = client.webSocketSession(
                host = host,
                port = port,
                path = path
            )
        } catch (e: Exception) {
            println("$tag:makeSureConnected: error: $e")
            e.printStackTrace()
            throw e
        }
    }

    suspend fun send(payload: ByteArray, retry: Boolean = true): ByteArray {

        println("$tag:send()")

        makeSureConnected()

        try {

            session?.let {

                it.send(Frame.Binary(fin = true, payload))

                val response = it.incoming.receive()

                println("$tag:send: got a response, type: '${response.frameType}', size: '${response.data.size}'")

                if (response is Frame.Binary) {
                    return response.readBytes()
                }

            }

        } catch (e: CancellationException) {
            println("$tag:send: exception ${e.message}")
            session?.close()
            session = null
            if (retry) {
                println("$tag:send: retrying...")
                send(payload, false)
            }
        } catch (e: Exception) {
            println("$tag:send: error: $e")
            e.printStackTrace()
        }

        return ByteArray(0)
    }

    suspend fun close() {
        println("$tag:close")
        session?.close()
        session = null
        client.close()
    }

}
