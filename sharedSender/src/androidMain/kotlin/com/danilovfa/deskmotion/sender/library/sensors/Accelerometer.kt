package com.danilovfa.deskmotion.sender.library.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

actual class Accelerometer(
    context: Context
) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var listener: SensorEventListener? = null

    private val _accelerometerFlow: MutableStateFlow<AccelerometerData?> = MutableStateFlow(null)
    actual val accelerometerFlow: StateFlow<AccelerometerData?> = _accelerometerFlow.asStateFlow()

    actual fun startListening() {
        listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let { sensorEvent ->
                    val accelerometerData = AccelerometerData(
                        dx = sensorEvent.values[0],
                        dy = sensorEvent.values[1],
                        dz = sensorEvent.values[2]
                    )

                    _accelerometerFlow.update {
                        accelerometerData
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        listener?.let { listener ->
            sensorManager.registerListener(listener, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    actual fun stopListening() {
        listener?.let { listener ->
            sensorManager.unregisterListener(listener)
        }
    }
}