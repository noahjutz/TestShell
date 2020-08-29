package com.example.testshell

object ShellExecutor {
    fun execute(command: String) = StringBuilder().apply {
        Runtime.getRuntime().exec(command).apply {
            waitFor()
            inputStream.reader().forEachLine { append(it) }
        }
    }.toString()
}