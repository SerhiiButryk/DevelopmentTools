package design_patterns.states.impl.states

import design_patterns.states.impl.TCPClient
import design_patterns.states.impl.TCPClientAPIs

/**
 * Base class for common state handling behavior
 */
open class TCPState(val state: ConnectionState, val client: TCPClient) : TCPClientAPIs {

    override fun listen() {
        super.listen()
    }

    override fun establish() {
        super.establish()
    }

    override fun connect() {
        super.connect()
    }

    override fun close() {
        super.close()
    }

    enum class ConnectionState(val state: Int, val description: String) {
        INITIAL(1, "INITIAL"),
        CONNECTED(2, "CONNECTED"),
        LISTENING(3, "LISTENING"),
        CLOSED(4, "CLOSED");
    }

}