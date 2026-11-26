package design_patterns.states.impl.states

import design_patterns.states.impl.TCPClient

/**
 * Represents TCP listening state
 */
class TCPListening(client: TCPClient) : TCPState(ConnectionState.LISTENING, client) {

    override fun listen() {
        super.listen()

        // Listening
    }

    override fun close() {
        super.close()

        // Stop listening

        // Then change the state
        client.changeState(TCPClosed(client), this)
    }

}