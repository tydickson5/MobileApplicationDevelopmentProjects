
package edu.virginia.cs4720.stepandactivitytracker

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.math.abs
import kotlin.math.sqrt


data class SensorReading(
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f,
    val acceleration: Float = 0f,
    val steps: Int = 0
)
class StepTracker(context: Context) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val stepSensor =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val dao = AppDb.getInstance(context).bucketDao()

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var lastStepTime = 0L
    private var steps = 0
    private var wasAboveThreshold = false

    private val threshold = 2.0f
    private val stepDelay = 300L

    val isAccelerometerAvailable: Boolean
        get() = stepSensor != null

    private val _sensorReading = MutableStateFlow(SensorReading())
    val sensorReading: StateFlow<SensorReading> = _sensorReading.asStateFlow()

    fun startTracking() {
        if (stepSensor == null) {
            Log.e("StepTracker", "Accelerometer unavailable")
            return
        }

        sensorManager.registerListener(
            this,
            stepSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        Log.d("StepTracker", "Accelerometer tracking started")
    }

    fun stopTracking() {
        sensorManager.unregisterListener(this)
        Log.d("StepTracker", "Tracking stopped")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        // Calculate total acceleration magnitude
        val magnitude = sqrt(x * x + y * y + z * z)

        // Remove gravity approximately
        val acceleration =
            abs(magnitude - SensorManager.GRAVITY_EARTH)

        Log.d(
            "StepTracker",
            "X=$x, Y=$y, Z=$z, Magnitude=$magnitude, Acceleration=$acceleration"
        )

        detectStep(acceleration)

        _sensorReading.value = SensorReading(
            x = x,
            y = y,
            z = z,
            acceleration = acceleration,
            steps = steps
        )
    }

    private fun detectStep(acceleration: Float) {
        val currentTime = System.currentTimeMillis()

        if (acceleration > threshold && !wasAboveThreshold) {

            if (currentTime - lastStepTime > stepDelay) {
                steps++

                lastStepTime = currentTime

                Log.d("StepTracker", "Steps: $steps")

                saveStep()
            }

            wasAboveThreshold = true
        }

        if (acceleration < threshold) {
            wasAboveThreshold = false
        }


    }

    private fun saveStep() {
        scope.launch {
            try {
                val today = LocalDate.now().toString()

                val existing = dao.getDay(today)

                val updatedSteps = (existing?.steps ?: 0) + 1

                val dayStat = DayStat(
                    date = today,
                    steps = updatedSteps,
                    sensorBaseline = 0
                )

                dao.insertOrUpdate(dayStat)

                Log.d(
                    "StepTracker",
                    "Saved daily steps: $updatedSteps"
                )

            } catch (e: Exception) {
                Log.e("StepTracker", "Failed to save steps", e)
            }
        }
    }

    // TEMPORARY TEST: Simulate three steps.
    // Call this from MainActivity or another test button.
    fun testStepDetection() {
        Log.d("StepTracker", "Starting simulated step test")

        val testValues = listOf(
            0.2f, 2.5f, 0.2f,
            0.2f, 2.8f, 0.2f,
            0.2f, 3.0f, 0.2f
        )

        scope.launch {
            testValues.forEach { value ->
                detectStep(value)
                kotlinx.coroutines.delay(400)
            }

            Log.d("StepTracker", "Simulated test complete")
        }
    }

    override fun onAccuracyChanged(
        sensor: Sensor?,
        accuracy: Int
    ) {
    }

    fun close() {
        stopTracking()
        scope.coroutineContext[kotlinx.coroutines.Job]?.cancel()
    }
}
