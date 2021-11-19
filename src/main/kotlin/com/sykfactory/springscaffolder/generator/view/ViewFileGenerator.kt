package com.sykfactory.springscaffolder.generator.view

import capitalToCamelCase
import com.sykfactory.springscaffolder.generator.FileGenerator
import com.sykfactory.springscaffolder.generator.FileGenerator.Companion.baseTemplatePath
import com.sykfactory.springscaffolder.util.createFileOnce
import snakeCase
import java.io.File
import java.lang.String.join

class ViewFileGenerator(
    private val path: String,
    modelClassName: String
) : FileGenerator {
    private val modelCamelName = modelClassName.capitalToCamelCase()
    private val modelPluralSnakeName = "${modelCamelName}s".snakeCase()

    override fun createFile() {
        createFormFile()
        createCreateFile()
        createShowFile()
        createIndexFile()
        createUpdateFile()
    }

    private fun createFormFile() {
        val file = createFileOnce(modelTemplatePath("form.html"))
    }

    private fun createCreateFile() {
        val file = createFileOnce(modelTemplatePath("create.html"))
    }

    private fun createShowFile() {
        val file = createFileOnce(modelTemplatePath("show.html"))
    }

    private fun createIndexFile() {
        val file = createFileOnce(modelTemplatePath("index.html"))
    }

    private fun createUpdateFile() {
        val file = createFileOnce(modelTemplatePath("update.html"))
    }

    private fun modelTemplatePath(filePath: String): String
        = join("/", baseTemplatePath, path, modelPluralSnakeName, filePath).replace("//", "/")
}