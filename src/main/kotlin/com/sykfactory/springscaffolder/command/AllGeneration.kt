package com.sykfactory.springscaffolder.command

import com.squareup.kotlinpoet.ClassName
import com.sykfactory.springscaffolder.Setting
import com.sykfactory.springscaffolder.extension.camelToSnakeCase
import com.sykfactory.springscaffolder.extension.capitalToCamelCase
import com.sykfactory.springscaffolder.generator.controller.ControllerFileGenerator
import com.sykfactory.springscaffolder.generator.model.ModelArguments
import com.sykfactory.springscaffolder.generator.model.ModelAttributeArgument
import com.sykfactory.springscaffolder.generator.model.ModelFileGenerator
import com.sykfactory.springscaffolder.generator.model.RepositoryFileGenerator
import com.sykfactory.springscaffolder.generator.view.BootstrapViewFileGenerator
import com.sykfactory.springscaffolder.generator.view.ViewFileGenerator
import kotlinx.cli.ArgType
import kotlinx.cli.Subcommand
import kotlinx.cli.vararg
import java.lang.String.join

class AllGeneration: Subcommand("all", "Generate Model, Repository, View, Controller") {
    private val modelNameDetail by argument(ArgType.String, description = "Model Name")
    private val attributes by argument(ArgType.String, description = "Model Attributes [name:type(=value)]").vararg()
    private val exclusion by option(ArgType.String, shortName = "e", description = "Skip File Generation Target")
    private var tableName by option(ArgType.String, shortName = "t", description = "Database Table Name")
    private var additionalControllerPackageName by option(ArgType.String, shortName = "c", description = "Controller Package Path")
    private var useBootstrap by option(ArgType.Boolean, fullName = "bootstrap", shortName = "b", description = "Use Bootstrap View File")

    override fun execute() {
        Setting.load()

        val modelName = modelNameDetail.substringAfterLast('.')
        val repositoryName = "${modelName}Repository"
        val controllerName = "${modelName}Controller"

        var additionalModelPackageName = ""
        var modelPackageName = Setting.modelBasePath
        var repositoryPackageName = Setting.repositoryBasePath
        var controllerPackageName = Setting.controllerBasePath

        additionalControllerPackageName = additionalControllerPackageName ?: ""

        if (modelNameDetail.contains('.')) {
            additionalModelPackageName = modelNameDetail.substringBeforeLast('.')
            modelPackageName = join(".", Setting.modelBasePath, additionalModelPackageName)
            repositoryPackageName = join(".", Setting.repositoryBasePath, additionalModelPackageName)
            if (additionalControllerPackageName == "") {
                additionalControllerPackageName = additionalModelPackageName
            }
        }
        if (additionalControllerPackageName != "") {
            controllerPackageName = join(".", Setting.controllerBasePath, additionalControllerPackageName)
        }

        val classNameModel = ClassName(modelPackageName, modelName)
        val classNameRepository = ClassName(repositoryPackageName, repositoryName)
        val classNameController = ClassName(controllerPackageName, controllerName)

        tableName = tableName ?: "${classNameModel.simpleName.capitalToCamelCase().camelToSnakeCase()}s"
        val modelArguments = ModelArguments(
            tableName,
            attributes.map {
                ModelAttributeArgument.parse(it)
            }
        )

        val exclusionTargets = exclusion?.replace(" ", "")?.split(',') ?: listOf()

        if (!exclusionTargets.contains("model")) {
            ModelFileGenerator(
                classNameModel,
                modelArguments
            ).generateFile()
        }

        if (!exclusionTargets.contains("repository")) {
            RepositoryFileGenerator(
                classNameRepository,
                classNameModel
            ).generateFile()
        }

        if (!exclusionTargets.contains("controller")) {
            ControllerFileGenerator(
                additionalControllerPackageName!!,
                classNameController,
                classNameModel,
                classNameRepository
            ).generateFile()
        }

        if (!exclusionTargets.contains("view")) {
            if (useBootstrap != null && useBootstrap!!) {
                BootstrapViewFileGenerator(
                    additionalControllerPackageName!!,
                    modelName,
                    modelArguments,
                    Setting.layoutPath
                ).generateFile()
            } else {
                ViewFileGenerator(
                    additionalControllerPackageName!!,
                    modelName,
                    modelArguments,
                    Setting.layoutPath
                ).generateFile()
            }
        }
    }
}