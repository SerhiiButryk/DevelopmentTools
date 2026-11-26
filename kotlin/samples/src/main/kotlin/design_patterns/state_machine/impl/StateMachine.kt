package design_patterns.state_machine.impl

/**
 * A state machine that handles states and their transitions in one place.
 * It only calls the client in order to process the current state.
 */
class StateMachine(private val client: TCPClientAPIs) {

    var currentState = ConnectionState.INITIAL
    private set

    enum class ConnectionState(val state: Int, val description: String) {
        INITIAL(1, "INITIAL"),
        CONNECTED(2, "CONNECTED"),
        LISTENING(3, "LISTENING"),
        CLOSED(4, "CLOSED"),
        FINISHED(5, "FINISHED");
    }

    fun startMachine() {
        println("$TAG.startMachine")
        processCurrentState()
    }

    private fun processCurrentState() {
        println("$TAG.processCurrentState $currentState")

        when(currentState) {
            ConnectionState.INITIAL -> client.establish()
            ConnectionState.CONNECTED -> client.connect()
            ConnectionState.LISTENING -> client.listen()
            ConnectionState.CLOSED -> client.close()
            else -> { }
        }

        if (currentState == ConnectionState.FINISHED) {
            println("$TAG.processCurrentState state machine is finished")
            return
        }

        nextState()
    }

    private fun nextState() {
        println("$TAG.nextState")

        when(currentState) {
            ConnectionState.INITIAL -> currentState = ConnectionState.CONNECTED
            ConnectionState.CONNECTED -> currentState = ConnectionState.LISTENING
            ConnectionState.LISTENING -> currentState = ConnectionState.CLOSED
            ConnectionState.CLOSED -> currentState = ConnectionState.FINISHED
            else -> { }
        }

        processCurrentState()
    }

    companion object {
        private const val TAG = "StateMachine"
    }

}