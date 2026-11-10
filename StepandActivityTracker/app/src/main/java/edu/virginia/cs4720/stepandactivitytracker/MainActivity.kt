package edu.virginia.cs4720.stepandactivitytracker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import edu.virginia.cs4720.stepandactivitytracker.ui.theme.StepAndActivityTrackerTheme

class MainActivity : ComponentActivity() {

    private lateinit var stepTracker: StepTracker

    private val requestPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                stepTracker.startTracking()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        stepTracker = StepTracker(this)
        //stepTracker.testStepDetection()

        stepTracker.startTracking()

        setContent {
            StepAndActivityTrackerTheme {
                setContent {
                    Surface(Modifier.fillMaxSize().safeDrawingPadding()) {
                        StepScreen(stepTracker = stepTracker)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        stepTracker.startTracking()
    }

    override fun onPause() {
        stepTracker.stopTracking()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        stepTracker.stopTracking()
    }
}