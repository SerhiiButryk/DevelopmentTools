package com.example.arduinocar.data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.telecom.Call
import android.util.Log

object GyroSensorsService : SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var gyroscope: Sensor? = null

    @Volatile
    var angle = 0.0
    private var lastTimestamp = 0L

    @Volatile
    var callback: Callback? = null

    interface Callback {
        fun onValueChanged(value: Double)
    }

    fun register(context: Context) {
        if (sensorManager == null) {
            sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            gyroscope = sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            gyroscope?.also {
                sensorManager!!.registerListener(
                    this,
                    it,
                    SensorManager.SENSOR_DELAY_GAME
                )
            }
        }
    }

    fun unregister() {
        sensorManager?.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.type != Sensor.TYPE_GYROSCOPE) return

            if (lastTimestamp != 0L) {
                val dt = (event.timestamp - lastTimestamp) / 1_000_000_000.0

                // Gyroscope is radians/second
                val angularVelocity = Math.toDegrees(event.values[2].toDouble())

                angle += angularVelocity * dt

                callback?.onValueChanged(angle)
            }

            lastTimestamp = event.timestamp
        }
    }
}