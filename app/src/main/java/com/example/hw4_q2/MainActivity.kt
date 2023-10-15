package com.example.hw4_q2

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity(), SensorEventListener{

    private lateinit var sensorManager: SensorManager
    private lateinit var square: TextView
    private var lastMoveTime: Long = 0
    private var isMovingRight = false
    private var isMovingLeft = false
    private var isMovingUp = false
    private var isMovingDown = false
    private var isMovingFront = false
    private var isMovingBack = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        square = findViewById<TextView>(R.id.tv_square)

        setupSensorStuff()
    }

    // setup the sensor/listener
    private fun setupSensorStuff() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        Log.e("sensor", "Sensor available")
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST, SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            // Retrieve values
            val xAxis = event.values[0]
            val yAxis = event.values[1]
            val zAxis = event.values[2]

            // Check for left or right movement
            if (xAxis > 2.0) {
                if (!isMovingRight && (System.currentTimeMillis() - lastMoveTime) > 1000) {
                    isMovingRight = true
                    isMovingLeft = false
                    showToast("Moved right")
                    lastMoveTime = System.currentTimeMillis()
                }
            } else if (xAxis < -2.0) {
                if (!isMovingLeft && (System.currentTimeMillis() - lastMoveTime) > 1000) {
                    isMovingLeft = true
                    isMovingRight = false
                    showToast("Moved left")
                    lastMoveTime = System.currentTimeMillis()
                } else {
                    isMovingRight = false
                    isMovingLeft = false
                }
            }

            // Check for up or down movement
            if (yAxis > 12.0) {
                if (!isMovingUp && (System.currentTimeMillis() - lastMoveTime) > 1000) {
                    isMovingUp = true
                    isMovingDown = false
                    isMovingRight = false
                    isMovingLeft = false
                    isMovingFront = false
                    isMovingBack = false
                    showToast("Moved up")
                    lastMoveTime = System.currentTimeMillis()
                }
            } else if (yAxis < -12.0) {
                if (!isMovingDown && (System.currentTimeMillis() - lastMoveTime) > 1000) {
                    isMovingUp = false
                    isMovingDown = true
                    isMovingRight = false
                    isMovingLeft = false
                    isMovingFront = false
                    isMovingBack = false
                    showToast("Moved down")
                    lastMoveTime = System.currentTimeMillis()
                } else {
                    isMovingRight = false
                    isMovingLeft = false
                    isMovingDown = false
                    isMovingUp = false
                    isMovingFront = false
                    isMovingBack = false
                }
            }

            // Check for front or back movement
            if (zAxis > 2.0) {
                if (!isMovingFront && (System.currentTimeMillis() - lastMoveTime) > 1000) {
                    isMovingFront = true
                    isMovingBack = false
                    isMovingUp = false
                    isMovingDown = false
                    isMovingRight = false
                    isMovingLeft = false
                    showToast("Moved front")
                    lastMoveTime = System.currentTimeMillis()
                }
            } else if (zAxis < -2.0) {
                if (!isMovingBack && (System.currentTimeMillis() - lastMoveTime) > 1000) {
                    isMovingFront = false
                    isMovingBack = true
                    isMovingUp = false
                    isMovingDown = false
                    isMovingRight = false
                    isMovingLeft = false
                    showToast("Moved back")
                    lastMoveTime = System.currentTimeMillis()
                } else {
                    isMovingBack = false
                    isMovingFront = false
                    isMovingRight = false
                    isMovingLeft = false
                    isMovingDown = false
                    isMovingUp = false
                }
            }


            var rotationX = -yAxis * 3f // Rotation around X-axis
            var rotationY = xAxis * 3f   // Rotation around Y-axis
            var rotationZ = zAxis * 3f   // Rotation around Z-axis

            square.apply {
                rotationX = rotationX
                rotationY = rotationY
                rotation = rotationZ // Rotation around Z-Axis

                translationX = xAxis * -10
                translationY = yAxis * -10
            }

            // Check if the phone is perfectly flat.. at rest
            val color = if (xAxis.toInt() == 0 && yAxis.toInt() == 0) Color.GREEN else Color.RED

            square.setBackgroundColor(color)
            square.text = "xAxis ${xAxis.toInt()}\n yAxis ${yAxis.toInt()}\nzAxis ${zAxis.toInt()}"
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
}

//need to make a Toast with the coordinates!
