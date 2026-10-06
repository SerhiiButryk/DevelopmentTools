package com.example.arduinocar.data

import android.util.Log
import com.example.arduinocar.ui.CarControl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.abs

class CarRepo : GyroSensorsService.Callback {

    private val tag = "CarControl"

    private var speedCurrent = 0
    private var speedDefault = 80
    private val speedMoveMax = 120
    private val speedTurnMax = 220
    private val speedTurnMin = 180
    private val invertDirection = false
    private val fastMode = true
    private var directionForward = false

    @Volatile
    private var turnSpeedDefault = 80

    // Protect our server and make sure that we do 1 request at a time
    private val dispatcher = Dispatchers.Default.limitedParallelism(1)

    private val root = "http://192.168.4.22"
    private val scope = CoroutineScope(dispatcher)

    @Volatile
    private var lastTimestamp = System.currentTimeMillis()

    @Serializable
    data class StateInfo(
        val batteryLevel: String,
        val voltage: String
    )

    init {
        GyroSensorsService.callback = this
    }

    fun getBatteryStateInfo() = flow {
        val jsonResponse = sendCommand(root)
        if (jsonResponse.isEmpty()) return@flow
        val info = Json.decodeFromString<StateInfo>(jsonResponse)
        val message = "Voltage: ${info.voltage}, \nBattery Level: ${info.batteryLevel}"
        emit(message)
    }

    suspend fun onStop() {
        speedCurrent = 0
        turnSpeedDefault = 0
        directionForward = false
        GyroSensorsService.angle = 0.0
        sendCommand()
    }

    suspend fun onMoveForward(state: Int) {
        if (state == CarControl.PRESSED) {
            speedCurrent = speedDefault
            if (speedCurrent > speedMoveMax)
                speedCurrent = speedMoveMax
            directionForward = true
            sendCommand()
        }
    }

    fun onSlowDown() {
    }

    suspend fun onMoveBackward(state: Int) {
        if (state == CarControl.PRESSED) {
            speedCurrent = speedDefault
            if (speedCurrent > speedMoveMax)
                speedCurrent = speedMoveMax
            directionForward = false
            sendCommand()
        }
    }

    fun onSpeedChanged(newValue: Int, state: Int) {
        if (state == CarControl.MOVE_SPEED) {
            if (newValue > speedMoveMax) {
                speedDefault = speedMoveMax
            } else {
                speedDefault = newValue
            }
        } else if (state == CarControl.TURN_SPEED) {
            if (newValue > speedTurnMax) {
                turnSpeedDefault = speedTurnMax
            } else {
                turnSpeedDefault = newValue
            }
        }
    }

    override fun onValueChanged(value: Double) {
        //Log.i(tag, "onValueChanged: start = $value")
        val curr = System.currentTimeMillis()
        if ((curr - lastTimestamp) > 500) {
            lastTimestamp = System.currentTimeMillis()
            val absolute = abs(value)
            if (absolute > 10) {
                val new = ((absolute * 8.4)).toInt()
                if (turnSpeedDefault == speedTurnMax && new >= speedTurnMax) return
                if (new < speedTurnMin) return
                turnSpeedDefault = new
                onSpeedChanged(turnSpeedDefault, CarControl.TURN_SPEED)
                GyroSensorsService.callback = null
                GyroSensorsService.angle = 0.0
                scope.launch {
                    if (value < 0) {
                        Log.i(tag, "onValueChanged: turn right $turnSpeedDefault")
                        sendCommand(genUrl(turnRight = true))
                    } else {
                        Log.i(tag, "onValueChanged: turn left $turnSpeedDefault")
                        sendCommand(genUrl(turnRight = false, turnLeft = true))
                    }
                    GyroSensorsService.callback = this@CarRepo
                }
            }
        }
    }

    fun onFastMode(enabled: Boolean) {
    }

    suspend fun onMoveRight(state: Int) {
        if (state == CarControl.PRESSED) {
            sendCommand(genUrl(turnRight = true))
        }
    }

    suspend fun onMoveLeft(state: Int) {
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

        val options = genOptions(directionForward, speedCurrent, fastMode)

        val path = if (turnRight) {
            "/motor?options=9&speed=$turnSpeedDefault"
        } else if (turnLeft) {
            "/motor?options=8&speed=$turnSpeedDefault"
        } else {
            "/motor?options=$options&speed=$speedCurrent"
        }

        return "$root$path"
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

    private suspend fun sendCommand(url: String? = null): String {
        val url = url ?: genUrl()
        Log.i(tag, "sendCommand: url - $url")
        var connection: HttpURLConnection? = null
        val response = StringBuilder()
        withContext(dispatcher) {
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

                    var line: String?

                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()

                    Log.i(tag, "sendCommand: success, response = $response")
                } else {
                    Log.i(tag, "sendCommand: error, response code = $responseCode")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.i(tag, "sendCommand: exception = $e")
            } finally {
                connection?.disconnect()
            }
        }
        return response.toString()
    }

}