package edu.virginia.cs4720.uvabucketlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object BucketRepository {
    var items by mutableStateOf(listOf<BucketItem>())
        private set

    fun add(item: BucketItem) { items = items + item }

    fun update(updated: BucketItem) {
        items = items.map { if (it.id == updated.id) updated else it }
    }
}
