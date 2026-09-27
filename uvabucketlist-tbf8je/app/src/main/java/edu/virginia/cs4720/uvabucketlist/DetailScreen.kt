package edu.virginia.cs4720.uvabucketlist

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.ZoneId
import androidx.lifecycle.viewmodel.compose.viewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    itemId: String,
    onDone: () -> Unit,
    vm: DetailViewModel = viewModel()
) {
    vm.start(itemId)

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Edit Item",
            )
            OutlinedTextField(
                value = vm.name,
                onValueChange = { vm.name = it },
                label = { Text("Item name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedButton(onClick = { vm.showDatePicker = true }) {
                Text(vm.dueDate?.toString() ?: "Pick due date")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = vm.completed,
                    onCheckedChange = { vm.toggleCompleted() }
                )
                Text(if (vm.completed) "Completed" else "Not completed")
            }

            Text(
                text = "Completed date: ${vm.completedDate?.toString() ?: "—"}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {
                        vm.save();
                        onDone() },
                    enabled = vm.canSave(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save")
                }
                OutlinedButton(
                    onClick = { onDone() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
            }
        }
    }

    if (vm.showDatePicker) {
        val state = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { vm.showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { millis ->
                        vm.dueDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.of("UTC"))
                            .toLocalDate()
                    }
                    vm.showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { vm.showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = state)
        }
    }
}