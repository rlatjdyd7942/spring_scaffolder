package com.sykfactory.springscaffolder.generator

interface FileGenerator {
    companion object {
        const val baseKotlinPath: String = "./src/main/kotlin"
        const val baseTemplatePath: String = "./src/main/resources/templates"
    }
    fun generateFile()
}