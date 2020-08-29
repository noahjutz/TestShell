package com.example.testshell

object ShellExecutor {
    fun execute(command: String) = StringBuilder().apply {
        try {
            Runtime.getRuntime().exec(command).apply {
                waitFor()
                inputStream.reader().forEachLine { append(it) }
            }
        } catch (e: Exception) {
            append("Error:\n")
            append(e.stackTraceToString())
        }
    }.toString().trim()
}