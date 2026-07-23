package com.web.repo

import com.ai.proto.Company
import com.web.net.WebSocketClient

class AIPromptManager {

    private val webSocketConnection = WebSocketClient()

    suspend fun sendRequest(query: String, systemPrompt: String): List<Company> {

        println("sendRequest() sending...")

        if (query.isBlank()) return emptyList()

        webSocketConnection.makeSureConnected()

        val message = com.ai.proto.AIMessage(systemPrompt, query)

        val payload = message.encode()

        val rawResponse = webSocketConnection.send(payload)
        val aiResponse = com.ai.proto.AIMessage.ADAPTER.decode(rawResponse)

        println("sendRequest: received items size = ${aiResponse.items.size}")

        return aiResponse.items
    }

    suspend fun onClose() {
        webSocketConnection.close()
    }

}