package com.noahjutz.testshell

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Icon
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope.gravity
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {
    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        setContent {
            MaterialTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {
                Content(clipboard)
            }
        }

        MobileAds.initialize(this) {}
    }
}

@Composable
fun Content(
    clipboard: ClipboardManager?
) {
    val input = remember { mutableStateOf("") }
    val result = remember { mutableStateOf("No output yet :(") }
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("TestShell") },
            navigationIcon = { IconButton(onClick = {}, icon = { Icon(Icons.Filled.AttachMoney) }) }
        )
    }) {
        ScrollableColumn(
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Row(
                verticalGravity = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            ) {
                TextField(
                    value = input.value,
                    onValueChange = { input.value = it },
                    label = { Text("Command") },
                    modifier = Modifier.padding(end = 16.dp)
                        .weight(1f)
                )
                IconButton(
                    icon = { Icon(Icons.Filled.Done) },
                    onClick = { result.value = ShellExecutor.execute(input.value) }
                )
            }
            Text(
                text = result.value,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            Button(
                onClick = {
                    clipboard!!.setPrimaryClip(ClipData.newPlainText("result", result.value))
                },
                content = {
                    Icon(Icons.Filled.ContentCopy)
                    Text("Copy output")
                },
                modifier = Modifier.gravity(Alignment.CenterHorizontally)
                    .gravity(Alignment.Bottom)
                    .padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
@Preview
fun ContentPreview() {
    MaterialTheme {
        Content(null)
    }
}