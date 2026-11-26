package design_patterns.state_machine

import design_patterns.state_machine.impl.TCPClientImpl

/**
 * Client class. It doesn't know about states and how they are handled.
 * It only initiates process. All details are hidden in TCPClientImpl class.
 */
class TCPClient {

    private val tcpClient = TCPClientImpl()

    fun openConnection() {
        tcpClient.initiateProcessing()
    }

}