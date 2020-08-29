package com.example.testshell

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {
                Content()
            }
        }
    }
}

@Composable
fun Content() {
    val result = remember { mutableStateOf("") }
    Scaffold(topBar = { TopAppBar(title = { Text("TestShell") }) }) {
        Column(
            horizontalGravity = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(24.dp)
        ) {
            Button(
                content = { Text("Click me!") },
                onClick = { result.value = ShellExecutor.execute("uname -a") },
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(result.value)
        }
    }
}

@Composable
@Preview
fun ContentPreview() {
    MaterialTheme {
        Content()
    }
}