package com.flexicon.tftdata.common

import kotlin.system.exitProcess
import kotlinx.cinterop.toKString
import kotlinx.coroutines.runBlocking
import platform.posix.getenv

fun runApplication() = runBlocking {
    runCatching {
        runDataWorker()
    }.onFailure {
        println("Error: $it")
        if (isDebug()) it.printStackTrace()
        exitProcess(1)
    }
}

fun isDebug() = listOf("true", "1").contains(getenv("DEBUG")?.toKString()?.lowercase())

