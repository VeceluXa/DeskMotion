package com.danilovfa.deskmotion.sender.library.sensors

import kotlinx.coroutines.flow.StateFlow

expect class Accelerometer {
    val accelerometerFlow: StateFlow<AccelerometerData?>
    fun startListening()
    fun stopListening()
}