package edu.virginia.cs4720.tbf8je

import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Label
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.virginia.cs4720.tbf8je.ui.theme.Helloworldtbf8jeTheme
import java.nio.file.WatchEvent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelloHeader()
        }
    }
}

@Composable
fun HelloHeader(){

    var name by remember { mutableStateOf(value = "") }

    val radioOptions = listOf("!", "?", ".")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    Column(modifier = Modifier.padding(24.dp)) {
        Text(
            text = "Hello $name$selectedOption",
            modifier = Modifier.padding(bottom = 20.dp)
        )
        TextField(
            value = name,
            onValueChange = {name = it},
            label = {Text("Enter your name") }
        )
        radioOptions.forEach { text ->
            Row(verticalAlignment = Alignment.CenterVertically){
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = { onOptionSelected(text) }
                )
                Text(text = text)
            }
        }
    }

}