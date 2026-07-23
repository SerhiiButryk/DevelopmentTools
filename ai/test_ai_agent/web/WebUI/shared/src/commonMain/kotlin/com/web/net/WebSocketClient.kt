package com.web.net

import com.web.ui.BuildKonfig
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.utils.io.CancellationException
import io.ktor.websocket.*

class WebSocketClient {

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
            println("makeSureConnected: already connected")
            return
        }
        try {
            println("makeSureConnected: connecting...")
            // Open WebSocket connection
            session = client.webSocketSession(
                host = host,
                port = port,
                path = path
            )
        } catch (e: Exception) {
            println("makeSureConnected: error: $e")
            e.printStackTrace()
            throw e
        }
    }

    suspend fun send(payload: ByteArray, retry: Boolean = true): ByteArray {

        println("send()")

        try {

            session?.let {

                it.send(Frame.Binary(fin = true, payload))

                val response = it.incoming.receive()

                if (response is Frame.Binary) {
                    println("send: got a response")
                    return response.readBytes()
                }

            }

        } catch (e: CancellationException) {
            session?.close()
            session = null
            makeSureConnected()
            if (retry) {
                println("send: retrying...")
                send(payload, false)
            }
        } catch (e: Exception) {
            println("send: error: $e")
            e.printStackTrace()
        }

        return ByteArray(0)
    }

    suspend fun close() {
        session?.close()
        session = null
        client.close()
    }

}
