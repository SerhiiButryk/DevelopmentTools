package com.example.arduinocar.data

import android.util.Log
import com.example.arduinocar.viewmodel.CarControl
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CarRepo {

    private val tag = "CarControl"

    private var speedCurrent = 0
    private var speedDefault = 80
    private val speedLimit = 120
    private val speedTurnLimit = 220
    private val speedIncreaseStep = 5
    private val invertDirection = false
    private val fastMode = true
    private var directionForward = false
    private var turnSpeedDefault = 200

    fun onStop() {
        speedCurrent = 0
        directionForward = false
        sendCommand()
    }

    fun onMoveForward(state: Int) {
        if (state == CarControl.PRESSED) {

            speedCurrent = speedDefault

            if (speedCurrent > speedLimit)
                speedCurrent = speedLimit

            directionForward = true
            sendCommand()
        }
    }

    fun onSlowDown() {
    }

    fun onMoveBackward(state: Int) {
        if (state == CarControl.PRESSED) {

            speedCurrent = speedDefault

            if (speedCurrent > speedLimit)
                speedCurrent = speedLimit

            directionForward = false
            sendCommand()
        }
    }

    fun onSpeedChanged(newValue: Int, state: Int) {
        if (state == CarControl.MOVE_SPEED) {
            if (newValue > speedLimit) {
                speedDefault = speedLimit
            } else {
                speedDefault = newValue
            }
        } else if (state == CarControl.TURN_SPEED) {
            if (newValue > speedTurnLimit) {
                turnSpeedDefault = speedLimit
            } else {
                turnSpeedDefault = newValue
            }
        }
    }

    fun onFastMode(enabled: Boolean) {
    }

    fun onMoveRight(state: Int) {
        if (state == CarControl.PRESSED) {
            sendCommand(genUrl(turnRight = true))
        }
    }

    fun onMoveLeft(state: Int) {
        if (state == CarControl.PRESSED) {
            sendCommand(genUrl(turnLeft = true))
        }
    }
/**
	Motor control REST API:

	http://192.168.4.22/motor?options=1&speed=80     // Forward gradually
	http://192.168.4.22/motor?options=2&speed=80     // Backward gradually
	http://192.168.4.22/motor?options=6&speed=80     // Backward fast mode
	http://192.168.4.22/motor?options=5&speed=80     // Forward fast mode
	http://192.168.4.22/motor?options=4&speed=0      // Stop
	http://192.168.4.22/motor?options=9&speed=220    // Turn right
	http://192.168.4.22/motor?options=8&speed=220    // Turn left
*/
    private fun genUrl(turnRight: Boolean = false, turnLeft: Boolean = false) : String {

        val host = "http://192.168.4.22"

        val options = genOptions(directionForward, speedCurrent, fastMode)

        val path = if (turnRight) {
            "/motor?options=9&speed=$turnSpeedDefault"
        } else if (turnLeft) {
            "/motor?options=8&speed=$turnSpeedDefault"
        } else {
            "/motor?options=$options&speed=$speedCurrent"
        }

        return "$host$path"
    }

    private fun genOptions(forward: Boolean, speed: Int, fastMode: Boolean): Int {
        if (speed == 0) {
            // Stop
            return 4
        }
        val directionForward = if (invertDirection) !forward else forward
        val options = if (directionForward) {
            if (fastMode) {
                5
            } else {
                1
            }
        } else {
            if (fastMode) {
                6
            } else {
                2
            }
        }
        return options
    }

    private fun sendCommand(url: String? = null) {
        val url = url ?: genUrl()
        Log.i(tag, "sendCommand: url - $url")
        var connection: HttpURLConnection? = null
        try {
            connection = URL(url)
                .openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the input stream
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuilder()
                var line: String?

                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()

                Log.i(tag, "sendCommand: success, response = $response")
                return
            } else {
                Log.i(tag, "sendCommand: error, response code = $responseCode")
                return
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i(tag, "sendCommand: exception = $e")
        } finally {
            connection?.disconnect()
        }
    }

}