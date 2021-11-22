package com.sykfactory.springscaffolder

import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter

object Setting {
    const val FILE_NAME = ".scaffold.yml"
    const val DEFAULT_MODEL_PATH = "model"
    const val DEFAULT_REPOSITORY_PATH = "repository"
    const val DEFAULT_CONTROLLER_PATH = "controller"
    const val DEFAULT_LAYOUT_FILE_PATH = "layout/layout"

    private val yaml = Yaml(
        DumperOptions().apply {
            defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
            isPrettyFlow = true
        }
    )

    var basePackageName: String = ""
    var modelPath: String = DEFAULT_MODEL_PATH
    var repositoryPath: String = DEFAULT_REPOSITORY_PATH
    var controllerPath: String = DEFAULT_CONTROLLER_PATH
    var layoutPath: String = DEFAULT_LAYOUT_FILE_PATH

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
            basePackageName = settings["basePackageName"]?.toString() ?: ""
            modelPath = settings["modelPath"]?.toString() ?: "model"
            repositoryPath = settings["repositoryPath"]?.toString() ?: "repository"
            controllerPath = settings["controllerPath"]?.toString() ?: "controller"
            layoutPath = settings["layoutFilePath"]?.toString() ?: "layout/layout"
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
            "controllerPath" to controllerPath,
            "layoutFilePath" to layoutPath
        )
        yaml.dump(settings, FileWriter(FILE_NAME))
    }
}