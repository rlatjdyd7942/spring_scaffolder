package com.sykfactory.springscaffolder.command

import com.sykfactory.springscaffolder.Setting
import com.sykfactory.springscaffolder.generator.model.ModelArguments
import com.sykfactory.springscaffolder.generator.model.ModelAttributeArgument
import com.sykfactory.springscaffolder.generator.model.ModelFileGenerator
import com.sykfactory.springscaffolder.generator.model.RepositoryFileGenerator
import kotlinx.cli.ArgType
import kotlinx.cli.Subcommand
import kotlinx.cli.vararg
import java.lang.String.join

class ModelGeneration: Subcommand("model", "Generate Model") {
    private val detailedModelPath by argument(ArgType.String, description = "Model Name")
    private val attributes by argument(ArgType.String, description = "Model Attributes [name:type(=value)]").vararg()
    private val tableName by option(ArgType.String, shortName = "t", description = "Database Table Name")

    override fun execute() {
        Setting.load()
        val modelPathSplit = detailedModelPath.split('.')
        val modelPackageName = if (modelPathSplit.size == 1) {
            Setting.modelPackageName
        } else {
            join(".", Setting.modelPackageName, modelPathSplit[0])
        }
        val modelName = modelPathSplit.last()
        val repositoryPackageName = if (modelPathSplit.size == 1) {
            Setting.repositoryPackageName
        } else {
            join(".", Setting.repositoryPackageName, modelPathSplit[0])
        }
        val repositoryName = "${modelName}Repository"
        ModelFileGenerator(
            modelPackageName,
            modelName,
            ModelArguments(
                tableName,
                attributes.map {
                    ModelAttributeArgument.parse(it)
                }
            )
        ).generateFile()
        RepositoryFileGenerator(
            repositoryPackageName,
            repositoryName,
            modelPackageName,
            modelName
        ).generateFile()
    }
}