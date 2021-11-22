package com.sykfactory.springscaffolder.command

import camelToSnakeCase
import capitalToCamelCase
import com.squareup.kotlinpoet.ClassName
import com.sykfactory.springscaffolder.Setting
import com.sykfactory.springscaffolder.generator.controller.ControllerFileGenerator
import com.sykfactory.springscaffolder.generator.model.ModelArguments
import com.sykfactory.springscaffolder.generator.model.ModelAttributeArgument
import com.sykfactory.springscaffolder.generator.model.ModelFileGenerator
import com.sykfactory.springscaffolder.generator.model.RepositoryFileGenerator
import com.sykfactory.springscaffolder.generator.view.ViewFileGenerator
import kotlinx.cli.ArgType
import kotlinx.cli.Subcommand
import kotlinx.cli.vararg
import java.lang.String.join

class AllGeneration: Subcommand("all", "Generate Model, Repository, View, Controller") {
    private val modelNameDetail by argument(ArgType.String, description = "Model Name")
    private val attributes by argument(ArgType.String, description = "Model Attributes [name:type(=value)]").vararg()
    private var tableName by option(ArgType.String, shortName = "t", description = "Database Table Name")
    private var additionalControllerPackageName by option(ArgType.String, shortName = "c", description = "Controller Package Path")

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

        ModelFileGenerator(
            classNameModel,
            modelArguments
        ).generateFile()

        RepositoryFileGenerator(
            classNameRepository,
            classNameModel
        ).generateFile()

        ControllerFileGenerator(
            additionalControllerPackageName!!,
            classNameController,
            classNameModel,
            classNameRepository
        ).generateFile()

        ViewFileGenerator(
            additionalControllerPackageName!!,
            modelName,
            modelArguments
        ).generateFile()
    }
}