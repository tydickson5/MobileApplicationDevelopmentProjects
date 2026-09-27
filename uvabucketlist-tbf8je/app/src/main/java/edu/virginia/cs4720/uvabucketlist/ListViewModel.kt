package edu.virginia.cs4720.uvabucketlist

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class ListViewModel : ViewModel() {
    fun items() = BucketRepository.items
    @RequiresApi(Build.VERSION_CODES.O)
    fun toggle(id: String) {
        val item = BucketRepository.items.first { it.id == id }
        BucketRepository.update(
            if (item.done) item.copy(done = false, completeDate = null)
            else item.copy(done = true, completeDate = LocalDate.now())
        )
    }
}