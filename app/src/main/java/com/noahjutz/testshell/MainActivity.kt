package com.noahjutz.testshell

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Icon
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope.gravity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.ui.tooling.preview.Preview
import com.google.android.gms.ads.*
import com.noahjutz.testshell.AdConstants.bannerId
import com.noahjutz.testshell.AdConstants.interstitialId
import com.noahjutz.testshell.AdConstants.testBannerId
import com.noahjutz.testshell.AdConstants.testInterstitialId

const val TAG = "MainActivity"
const val DEBUG = BuildConfig.BUILD_TYPE != "release"

class MainActivity : AppCompatActivity() {
    private lateinit var interstitialAd: InterstitialAd

    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        setContent {
            MaterialTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {
                Content(clipboard, ::showInterstitial)
            }
        }

        MobileAds.initialize(this)
        interstitialAd = InterstitialAd(this).apply {
            adUnitId = if (DEBUG) testInterstitialId else interstitialId
            loadAd(AdRequest.Builder().build())
        }
        Log.d(TAG, "DEBUG: $DEBUG")
    }

    private fun showInterstitial() {
        interstitialAd.apply { if (isLoaded) show() else Log.d(TAG, "Not loaded") }
    }
}

@Composable
fun Content(
    clipboard: ClipboardManager?,
    showInterstitial: () -> Unit
) {
    val input = remember { mutableStateOf("") }
    val result = remember { mutableStateOf("No output yet :(") }
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "TestShell", fontFamily = FontFamily.Monospace) },
            navigationIcon = { IconButton(onClick = {}, icon = { Icon(Icons.Filled.AttachMoney) }) }
        )
    }) {
        Column {
            ScrollableColumn(
                modifier = Modifier.fillMaxWidth()
                    .weight(1f)
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Row(
                    verticalGravity = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                ) {
                    TextField(
                        value = input.value,
                        onValueChange = { input.value = it },
                        label = { Text(text = "Command", fontFamily = FontFamily.Monospace) },
                        modifier = Modifier.padding(end = 16.dp)
                            .weight(1f)
                    )
                    IconButton(
                        icon = { Icon(Icons.Filled.Done) },
                        onClick = {
                            result.value = ShellExecutor.execute(input.value)
                            showInterstitial()
                        }
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
                        Icon(
                            asset = Icons.Filled.ContentCopy,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(text = "Copy output")
                    },
                    modifier = Modifier.gravity(Alignment.CenterHorizontally)
                        .gravity(Alignment.Bottom)
                        .padding(bottom = 16.dp)
                )
            }
            Surface(modifier = Modifier.gravity(Alignment.CenterHorizontally)) {
                AdBanner()
            }
        }
    }
}

@Composable
fun AdBanner() {
    val context = ContextAmbient.current
    val customView = remember { AdView(context) }
    AndroidView(viewBlock = { customView }) {
        it.apply {
            adSize = AdSize.BANNER
            adUnitId = if (DEBUG) testBannerId else bannerId
            loadAd(AdRequest.Builder().build())
        }
    }
}

@Composable
@Preview
fun ContentPreview() {
    MaterialTheme {
        Content(null, {})
    }
}

object AdConstants {
    const val bannerId = "ca-app-pub-7450355346929746/8415970128"
    const val testBannerId = "ca-app-pub-3940256099942544/6300978111"

    const val interstitialId = "ca-app-pub-7450355346929746/2214358000"
    const val testInterstitialId = "ca-app-pub-3940256099942544/1033173712"

    const val appId = "ca-app-pub-7450355346929746~3924214862"
}