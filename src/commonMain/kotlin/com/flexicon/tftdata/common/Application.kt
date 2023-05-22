package com.flexicon.tftdata.common

import kotlinx.cinterop.toKString
import kotlinx.coroutines.runBlocking
import platform.posix.getenv

fun runApplication() = runBlocking {
    runCatching {
        runDataWorker()
    }.onFailure {
        println("Error: $it")
        if (isDebug()) it.printStackTrace()
    }
}

fun isDebug() = listOf("true", "1").contains(getenv("DEBUG")?.toKString()?.lowercase())

