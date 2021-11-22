package com.sykfactory.springscaffolder.command

import com.squareup.kotlinpoet.ClassName
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
    private val modelNameDetail by argument(ArgType.String, description = "Model Name")
    private val attributes by argument(ArgType.String, description = "Model Attributes [name:type(=value)]").vararg()
    private val tableName by option(ArgType.String, shortName = "t", description = "Database Table Name")

    override fun execute() {
        Setting.load()
        val modelName = modelNameDetail.substringAfterLast('.')
        val repositoryName = "${modelName}Repository"

        var modelPackageName = Setting.modelBasePath
        var repositoryPackageName = Setting.repositoryBasePath

        if (modelNameDetail.contains('.')) {
            modelPackageName = join(".", Setting.modelBasePath, modelNameDetail.substringBeforeLast('.'))
            repositoryPackageName = join(".", Setting.repositoryBasePath, modelNameDetail.substringBeforeLast('.'))
        }

        val classNameModel = ClassName(modelPackageName, modelName)
        val classNameRepository = ClassName(repositoryPackageName, repositoryName)

        ModelFileGenerator(
            classNameModel,
            ModelArguments(
                tableName,
                attributes.map {
                    ModelAttributeArgument.parse(it)
                }
            )
        ).generateFile()
        RepositoryFileGenerator(
            classNameRepository,
            classNameModel
        ).generateFile()
    }
}