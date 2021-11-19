package com.sykfactory.springscaffolder.generator

import com.sykfactory.springscaffolder.generator.FileGenerator.Companion.baseKotlinPath
import com.sykfactory.springscaffolder.util.removeTextFromFile

abstract class ClassFileGenerator (
    open val packageName: String,
    open val className: String
): FileGenerator {
    fun generateFile() {
        createFile()
        val filePath = getFilePath(packageName, null, "$className.kt")
        removeTextFromFile(filePath, "public ", ": Unit")
    }

    private fun getFilePath(basePackageName: String, additionalPackageName: String?, fileName: String): String {
        return additionalPackageName?.let {
            java.lang.String.join("/", baseKotlinPath, toPath(basePackageName), toPath(additionalPackageName), fileName)
        } ?: java.lang.String.join("/", baseKotlinPath, toPath(basePackageName), fileName)
    }

    private fun toPath(packageName: String): String {
        return packageName.replace(".", "/")
    }
}
