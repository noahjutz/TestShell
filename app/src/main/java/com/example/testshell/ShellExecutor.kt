package com.example.testshell

import android.util.Log

object ShellExecutor {
    private const val TAG = "ShellExecutor"

    fun execute(command: String): String {
        Log.d(TAG, "execute: $command")
        val result = StringBuilder()
        try {
            Runtime.getRuntime().exec(command).apply {
                waitFor()
                inputStream.reader().forEachLine {
                    result.append(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.d(TAG, "execute: $result")
        return result.toString()
    }
}