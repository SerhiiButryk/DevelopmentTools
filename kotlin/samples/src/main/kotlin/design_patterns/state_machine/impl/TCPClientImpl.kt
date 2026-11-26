package design_patterns.state_machine.impl

/**
 * Class which should do the actual work depending on current
 * TCP connection state. However, it doesn't know about all internal states
 * and how they are managed.
 */
class TCPClientImpl : TCPClientAPIs {

    private val stateMachine = StateMachine(this)

    fun initiateProcessing() {
        stateMachine.startMachine()
    }

    override fun listen() {
        println("$TAG.listen, in state: ${stateMachine.currentState}")
    }

    override fun establish() {
        println("$TAG.establish, in state: ${stateMachine.currentState}")
    }

    override fun connect() {
        println("$TAG.connect, in state: ${stateMachine.currentState}")
    }

    override fun close() {
        println("$TAG.close, in state: ${stateMachine.currentState}")
    }

    companion object {
        private const val TAG = "TCPClient"
    }

}