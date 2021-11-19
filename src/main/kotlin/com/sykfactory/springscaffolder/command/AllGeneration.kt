package com.sykfactory.springscaffolder.command

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
import java.lang.String

class AllGeneration: Subcommand("all", "Generate Model, Repository, View, Controller") {
    private val detailedModelPath by argument(ArgType.String, description = "Model Name")
    private val attributes by argument(ArgType.String, description = "Model Attributes [name:type(=value)]").vararg()
    private val tableName by option(ArgType.String, shortName = "t", description = "Database Table Name")

    override fun execute() {
        Setting.load()

        val modelPathSplit = detailedModelPath.split('.')
        val modelPackageName = if (modelPathSplit.size == 1) {
            Setting.modelPackageName
        } else {
            String.join(".", Setting.modelPackageName, modelPathSplit[0])
        }
        val modelName = modelPathSplit.last()
        val modelArguments = ModelArguments(
            tableName,
            attributes.map {
                ModelAttributeArgument.parse(it)
            }
        )
        ModelFileGenerator(
            modelPackageName,
            modelName,
            modelArguments
        ).generateFile()

        val repositoryPackageName = if (modelPathSplit.size == 1) {
            Setting.repositoryPackageName
        } else {
            String.join(".", Setting.repositoryPackageName, modelPathSplit[0])
        }
        val repositoryName = "${modelName}Repository"
        RepositoryFileGenerator(
            repositoryPackageName,
            repositoryName,
            modelPackageName,
            modelName
        ).generateFile()

        val controllerPackageName = if (modelPathSplit.size == 1) {
            Setting.controllerPackageName
        } else {
            String.join(".", Setting.controllerPackageName, modelPathSplit[0])
        }
        val controllerName = "${modelName}Controller"
        ControllerFileGenerator(
            controllerPackageName,
            controllerName,
            modelPackageName,
            modelName,
            modelArguments,
            repositoryPackageName,
            repositoryName
        ).generateFile()

        val viewPath = if (modelPathSplit.size == 1) {
            ""
        } else {
            modelPathSplit[0].replace(".", "/")
        }
        ViewFileGenerator(
            viewPath,
            modelName
        ).createFile()
    }
}