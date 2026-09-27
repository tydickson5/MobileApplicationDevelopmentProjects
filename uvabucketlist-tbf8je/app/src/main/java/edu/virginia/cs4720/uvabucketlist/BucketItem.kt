package edu.virginia.cs4720.uvabucketlist
import java.time.LocalDate
import java.util.UUID

data class BucketItem (
    var id: String = UUID.randomUUID().toString(),
    val name: String,
    var dueDate: LocalDate,
    var done: Boolean = false,
    var completeDate: LocalDate? = null
)