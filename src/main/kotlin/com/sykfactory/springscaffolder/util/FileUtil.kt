package com.sykfactory.springscaffolder.util

import java.io.File

fun createFileOnce(filePath: String): File {
    val directory = File(filePath.substringBeforeLast('/'))
    if (!directory.exists()) {
        directory.mkdirs()
    }
    return File(filePath).also {
        it.createNewFile()
    }
}

fun removeTextFromFile(filePath: String, vararg toRemove: String) {
    val file = File(filePath)
    var text = file.readText()
    toRemove.forEach {
        text = text.replace(it, "")
    }
    file.writeText(text)
}