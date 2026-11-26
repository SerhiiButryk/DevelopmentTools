package design_patterns.states.impl.states

import design_patterns.states.impl.TCPClient

/**
 * Represents TCP connected state to remote
 */
class TCPConnected(client: TCPClient) : TCPState(ConnectionState.CONNECTED, client) {

    override fun connect() {
        super.connect()

        // Connecting ...

        // Then change state
        client.changeState(TCPListening(client), this)
    }

    override fun close() {
        super.close()

        // Close connection

        // Then change the state
        client.changeState(TCPClosed(client), this)
    }

}