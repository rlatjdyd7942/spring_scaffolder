package com.sykfactory.springscaffolder.generator

import com.squareup.kotlinpoet.ClassName
import com.sykfactory.springscaffolder.generator.FileGenerator.Companion.baseKotlinPath
import com.sykfactory.springscaffolder.util.removeTextFromFile

abstract class ClassFileGenerator (
    open val className: ClassName
): FileGenerator {
    abstract fun createFile()

    override fun generateFile() {
        createFile()
        val filePath = getFilePath(className.packageName, null, "${className.simpleName}.kt")
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
