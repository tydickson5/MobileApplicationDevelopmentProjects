
package edu.virginia.cs4720.stepandactivitytracker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel



@Composable
fun StepScreen(stepTracker: StepTracker, viewModel: DayStatViewModel = viewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sensorReading by stepTracker.sensorReading.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    if (!stepTracker.isAccelerometerAvailable) {
        Text("Accelerometer is unavailable on this device.")
    } else {
        when (val state = uiState) {

            is UIState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UIState.Success -> {

                if (state.days.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No step data yet")
                    }
                } else {

                    val maxSteps = maxOf(
                        state.days.maxOf { it.steps },
                        1
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    "Live Accelerometer",
                                    style = MaterialTheme.typography.headlineSmall
                                )

                                Text("X: %.3f".format(sensorReading.x))
                                Text("Y: %.3f".format(sensorReading.y))
                                Text("Z: %.3f".format(sensorReading.z))

                                Text(
                                    "Acceleration: %.3f m/s²".format(
                                        sensorReading.acceleration
                                    )
                                )

                                Text(
                                    "Session Steps: ${sensorReading.steps}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                        item {
                            Text(
                                text = "Daily Steps",
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }

                        items(state.days) { day ->

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = day.date,
                                        style = MaterialTheme.typography.bodyLarge
                                    )

                                    Text(
                                        text = "${day.steps} steps",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                Canvas(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(16.dp)
                                ) {
                                    val barWidth =
                                        size.width * (day.steps.toFloat() / maxSteps)

                                    // Background bar
                                    drawRoundRect(
                                        color = Color.LightGray,
                                        size = Size(size.width, size.height),
                                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx())
                                    )

                                    // Steps bar
                                    drawRoundRect(
                                        color = Color(0xFF4CAF50),
                                        size = Size(barWidth, size.height),
                                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx())
                                    )
                                }
                            }
                        }
                    }
                }
            }

            is UIState.Error -> {
                Text(state.message)
            }
        }


    }
}
