package design_patterns.states.impl

/**
 * Methods which should work differently depending on TCP state.
 */
interface TCPClientAPIs {

    fun listen() {
        println("$TAG.listen")
    }

    fun establish() {
        println("$TAG.establish")
    }

    fun connect() {
        println("$TAG.connect")
    }

    fun close() {
        println("$TAG.close")
    }

    companion object {
        private const val TAG = "TCPClientAPIs"
    }

}