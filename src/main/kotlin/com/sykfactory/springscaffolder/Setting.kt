package com.sykfactory.springscaffolder

import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter

object Setting {
    const val FILE_NAME = ".scaffold.yml"
    private val yaml = Yaml(
        DumperOptions().apply {
            defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
            isPrettyFlow = true
        }
    )

    var basePackageName: String? = null
    var modelPath: String? = null
    var repositoryPath: String? = null
    var controllerPath: String? = null

    val modelBasePath: String
        get() {
            return "$basePackageName.$modelPath"
        }
    val repositoryBasePath: String
        get() {
            return "$basePackageName.$repositoryPath"
        }
    val controllerBasePath: String
        get() {
            return "$basePackageName.$controllerPath"
        }

    fun load() {
        try {
            val file = File(FILE_NAME)
            val settings: Map<String, Any> = yaml.load(file.inputStream())
            basePackageName = settings["basePackageName"]?.toString()
            modelPath = settings["modelPath"]?.toString()
            repositoryPath = settings["repositoryPath"]?.toString()
            controllerPath = settings["controllerPath"]?.toString()
        } catch (e: FileNotFoundException) {
            println("Run \"scaffold init\" first")
            throw e
        }
    }

    fun save() {
        val settings = mapOf(
            "basePackageName" to basePackageName,
            "modelPath" to modelPath,
            "repositoryPath" to repositoryPath,
            "controllerPath" to controllerPath
        )
        yaml.dump(settings, FileWriter(FILE_NAME))
    }
}