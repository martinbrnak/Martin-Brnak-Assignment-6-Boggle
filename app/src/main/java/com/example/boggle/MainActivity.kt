package com.example.boggle

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.boggle.GameFragment
import com.example.boggle.R
import java.util.Objects
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    private lateinit var gameFragment: GameFragment

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameFragment = GameFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, gameFragment)
            .commit()

        val btnReset: android.widget.Button = findViewById(R.id.btnNewGame)
        btnReset.setOnClickListener {
            gameFragment.resetGame()
        }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Objects.requireNonNull(sensorManager)!!
            .registerListener(
                sensorListener,
                sensorManager!!
                    .getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL
            )

        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH
    }

    private var lastShakeTime: Long = 0
    private val shakeDelay: Long = 2000 // 2 seconds delay
    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {

            val currentTime = System.currentTimeMillis()

            if (currentTime - lastShakeTime >= shakeDelay) {

                // Fetching x,y,z values
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                lastAcceleration = currentAcceleration

                // Getting current accelerations
                // with the help of fetched x,y,z values
                currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
                val delta: Float = currentAcceleration - lastAcceleration
                acceleration = acceleration * 0.9f + delta

                // Display a Toast message if
                // acceleration value is over 12
                if (acceleration > 15) {
                    gameFragment.resetGame()
                    Toast.makeText(
                        applicationContext,
                        "Shake detected, resetting game",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                lastShakeTime = currentTime
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // No implementation needed here
        }
    }
}
