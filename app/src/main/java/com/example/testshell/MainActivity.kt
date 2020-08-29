package com.example.testshell

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
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
    Scaffold(topBar = { TopAppBar(title = { Text("TestShell") }) }) {
        Column(
                horizontalGravity = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().fillMaxHeight()
        ) {
            Button(
                    content = { Text("Click me!") },
                    onClick = {ShellExecutor.execute("echo \"Hello World\"")}
            )
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