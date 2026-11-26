package design_patterns.states.impl

import design_patterns.states.impl.states.TCPInitial
import design_patterns.states.impl.states.TCPState

/**
 * Simplified TCP Clint which has internal states.
 * When state is changed, behavior of the class is also changed.
 *
 * Note that class methods delegate its work to TCP State object.
 * It will do necessary work depending on state.
 */
class TCPClient : TCPClientAPIs {

    private var currentState : TCPState = TCPInitial(this)

    override fun listen() {
        println("$TAG.listen in state: ${currentState.state}")
        currentState.listen()
    }

    override fun establish() {
        println("$TAG.establish in state: ${currentState.state}")
        currentState.establish()
    }

    override fun connect() {
        println("$TAG.connect in state: ${currentState.state}")
        currentState.connect()
    }

    override fun close() {
        println("$TAG.close in state: ${currentState.state}")
        currentState.close()
    }

    fun changeState(newState: TCPState, oldState: TCPState) {
        println("$TAG.changeState new: $newState, old: $oldState")
        // There can be state checks. Skipped for simplicity.
        currentState = newState
    }

    companion object {
        private const val TAG = "TCPClient"
    }

}