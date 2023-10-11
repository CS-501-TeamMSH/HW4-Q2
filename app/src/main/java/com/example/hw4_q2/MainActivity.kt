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
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity(), SensorEventListener{

    private lateinit var sensorManager: SensorManager
    private lateinit var square: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        square = findViewById<TextView>(R.id.tv_square)

        setupSensorStuff()

    }

    //setup the sensor/listener!
    private fun setupSensorStuff() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        Log.e("senor", "Sensor available")
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(this, it,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_FASTEST)
        }

    }

//retreive the listener

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            //retreive values!
            val xAxis = event.values[0] //right & left values
            val yAxis = event.values[1] // up and down
            val zAxis = event.values[2] //forward and backward

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


            //check if phone is perfectly flat.. at rest
            val color = if(xAxis.toInt() == 0 &&  yAxis.toInt() == 0) Color.GREEN else Color.RED
            //val isAtRest = xAxis > -1 && xAxis < 1 && yAxis > -1 && yAxis < 1
            //val color = if (isAtRest) Color.GREEN else Color.RED

            square.setBackgroundColor(color)

            square.text = "xAxis ${xAxis.toInt()}\n yAxis ${yAxis.toInt()}\nzAxis ${zAxis.toInt()}"

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
