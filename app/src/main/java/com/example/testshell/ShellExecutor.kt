package com.example.testshell

import android.util.Log

object ShellExecutor {
    private const val TAG = "ShellExecutor"

    fun execute(command: String) = StringBuilder().apply {
        try {
            Runtime.getRuntime().exec(command).apply {
                waitFor()
                inputStream.reader().forEachLine {
                    append(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.toString()
}