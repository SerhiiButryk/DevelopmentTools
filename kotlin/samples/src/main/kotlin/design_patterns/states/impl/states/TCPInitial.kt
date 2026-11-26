package design_patterns.states.impl.states

import design_patterns.states.impl.TCPClient

/**
 * Represents TCP initial state
 */
class TCPInitial(client: TCPClient) : TCPState(ConnectionState.INITIAL, client) {

    override fun establish() {
        super.establish()

        // Preparing for TCP connection ...

        // Then change the state
        client.changeState(TCPConnected(client), this)
    }

}