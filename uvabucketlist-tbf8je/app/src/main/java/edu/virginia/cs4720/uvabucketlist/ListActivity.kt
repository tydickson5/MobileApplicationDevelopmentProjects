package edu.virginia.cs4720.uvabucketlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import edu.virginia.cs4720.uvabucketlist.ui.theme.UVABucketListTheme

class ListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { UVABucketListTheme {
            ListScreen()
        } }
    }
}