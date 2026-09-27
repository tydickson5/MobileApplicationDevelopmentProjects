package edu.virginia.cs4720.uvabucketlist

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class DetailViewModel : ViewModel() {

    var id by mutableStateOf("")
    var name by mutableStateOf("")
    var dueDate by mutableStateOf<LocalDate?>(null)
    var showDatePicker by mutableStateOf(false)
    var completed by mutableStateOf(false)
    var completedDate by mutableStateOf<LocalDate?>(null)

    private var loadedId: String? = null

    fun start(itemId: String) {
        if (loadedId != null) return // only the first call loads

        val item = BucketRepository.items.first { it.id == itemId }
        loadedId = itemId

        id = item.id
        name = item.name
        dueDate = item.dueDate
        completed = item.done
        completedDate = item.completeDate
    }

    fun canSave(): Boolean = name.isNotBlank() && dueDate != null

    @RequiresApi(Build.VERSION_CODES.O)
    fun toggleCompleted() {
        completed = !completed
        completedDate = if (completed) LocalDate.now() else null
    }

    fun save() {
        BucketRepository.update(
            BucketItem(
                id = id,
                name = name,
                dueDate = dueDate!!,
                done = completed,
                completeDate = completedDate
            )
        )
    }
}