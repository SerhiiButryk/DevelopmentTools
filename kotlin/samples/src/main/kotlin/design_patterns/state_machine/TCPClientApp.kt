package design_patterns.state_machine

/**
 * State machine pattern
 *
 * Program: TCP client application.
 *
 * Description: This is a variation of state pattern implementation.
 *
 * Compared to State pattern, it requires fewer classes for implementation.
 * Although there is no need for state object creation because there is no state object
 * (as states are represented as enum). All states and their transitions are managed by a single class.
 * However, TCPClientImpl class can grow. New conditions and states can be added.
 * Probably, there can be a lot of if-else condition switches.
 */
fun main() {

    // Class which has internal states
    // and should behave differently according to the current internal state.
    val tcpClient = TCPClient()

    // 1. Initiate state transition and processing
    // INITIAL -> CONNECTED -> LISTENING -> CLOSED -> FINISHED
    tcpClient.openConnection()
}