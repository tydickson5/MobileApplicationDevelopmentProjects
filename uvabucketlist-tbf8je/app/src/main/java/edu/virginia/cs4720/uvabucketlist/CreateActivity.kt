package edu.virginia.cs4720.uvabucketlist

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import edu.virginia.cs4720.uvabucketlist.ui.theme.UVABucketListTheme

// CreateActivity.kt
class CreateActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { UVABucketListTheme() {
            CreateScreen(onDone = { finish() })
        } }
    }
}