package com.jakarispann.flashcard

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt

class SensorActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    // UI references
    private lateinit var tvStatus: TextView
    private lateinit var tvX: TextView
    private lateinit var tvY: TextView
    private lateinit var tvZ: TextView
    private lateinit var tvShakeCount: TextView
    private lateinit var tvShakeMessage: TextView

    // Shake detection state
    private var shakeCount = 0
    private var lastShakeTime = 0L

    // Shake sensitivity threshold (m/s²) — adjust higher to require stronger shakes
    private val SHAKE_THRESHOLD = 12.0f
    // Minimum ms between registered shakes (prevents double-counting)
    private val SHAKE_COOLDOWN_MS = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)

        // Bind views
        tvStatus       = findViewById(R.id.tvSensorStatus)
        tvX            = findViewById(R.id.tvAxisX)
        tvY            = findViewById(R.id.tvAxisY)
        tvZ            = findViewById(R.id.tvAxisZ)
        tvShakeCount   = findViewById(R.id.tvShakeCount)
        tvShakeMessage = findViewById(R.id.tvShakeMessage)

        // Obtain the SensorManager system service
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Try to get the accelerometer — it may be null on some devices/emulators
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (accelerometer == null) {
            // Graceful degradation: inform the user the sensor is unavailable
            tvStatus.text = "⚠️ Accelerometer not available on this device."
        } else {
            tvStatus.text = "✅ Accelerometer ready. Move or shake the device!"
        }
    }

    // Register listener when activity is visible — best-practice lifecycle management
    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_UI   // ~60ms updates, sufficient for display
            )
        }
    }

    // Unregister listener when activity is paused — saves battery and prevents leaks
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    // Called every time the sensor produces a new reading
    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        // Display real-time axis values (rounded to 2 decimal places)
        tvX.text = "X: ${"%.2f".format(x)} m/s²"
        tvY.text = "Y: ${"%.2f".format(y)} m/s²"
        tvZ.text = "Z: ${"%.2f".format(z)} m/s²"

        // Compute the net acceleration magnitude, removing gravity (9.8 m/s² on Z at rest)
        // We use the raw vector length for simplicity; threshold is set above resting ~9.8
        val magnitude = sqrt((x * x + y * y + z * z).toDouble()).toFloat()

        // Detect shake: magnitude above threshold AND cooldown period has passed
        val now = System.currentTimeMillis()
        if (magnitude > SHAKE_THRESHOLD && (now - lastShakeTime) > SHAKE_COOLDOWN_MS) {
            lastShakeTime = now
            shakeCount++
            tvShakeCount.text = "Shakes detected: $shakeCount"

            // Cycle through fun messages to keep feedback interesting
            val messages = listOf(
                "📚 Shake it to study harder!",
                "🔀 Flashcards shuffled!",
                "💡 Keep shaking to learn!",
                "🎉 Great shake! You're on a roll!",
                "⚡ Shake detected — nice one!"
            )
            tvShakeMessage.text = messages[shakeCount % messages.size]
        }
    }

    // Required by SensorEventListener — called when sensor accuracy changes
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No action needed for accelerometer accuracy changes in this app
    }
}
