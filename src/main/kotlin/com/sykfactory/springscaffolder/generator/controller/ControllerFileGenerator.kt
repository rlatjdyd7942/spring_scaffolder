package com.sykfactory.springscaffolder.generator.controller

import capitalToCamelCase
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.sykfactory.springscaffolder.generator.ClassFileGenerator
import com.sykfactory.springscaffolder.generator.FileGenerator
import com.sykfactory.springscaffolder.generator.model.ModelArguments
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import snakeCase
import kotlin.io.path.Path

class ControllerFileGenerator(
    override val packageName: String,
    override val className: String,
    private val modelPackageName: String,
    private val modelClassName: String,
    private val modelArguments: ModelArguments,
    private val repositoryPackageName: String,
    private val repositoryClassName: String
): ClassFileGenerator(packageName, className) {
    private val repositoryAttributeName = repositoryClassName.capitalToCamelCase()
    private val modelCamelName = modelClassName.capitalToCamelCase()
    private val modelPluralName = "${modelCamelName}s"
    private val modelPluralSnakeName = modelPluralName.snakeCase()

    override fun createFile() {
        var attributeBuilder = FunSpec.constructorBuilder().apply {
            addParameter(
                ParameterSpec.builder(
                    repositoryAttributeName, ClassName(repositoryPackageName, repositoryClassName)
                ).build()
            )
        }
        var classTypeSpec = TypeSpec.classBuilder(className).apply {
            primaryConstructor(attributeBuilder.build())
            addProperty(
                PropertySpec.builder(
                    repositoryAttributeName, ClassName(repositoryPackageName, repositoryClassName), KModifier.PRIVATE
                ).initializer(repositoryAttributeName).build()
            )
            addAnnotation(RestController::class.asClassName())
            addAnnotation(
                AnnotationSpec.builder(
                    RequestMapping::class.asClassName()
                ).addMember("\"/$modelPluralSnakeName\"").build()
            )
            addFunctions(listOf(getCreateFunSpec(), getReadFunSpec()))
        }.build()
        FileSpec.builder(packageName, className).apply {
            addImport("org.springframework.data.repository", "findByIdOrNull")
            addImport(ModelAndView::class, listOf())
            addType(classTypeSpec)
        }.build().writeTo(Path(FileGenerator.baseKotlinPath))
    }

    private fun getCreateFunSpec(): FunSpec {
        val mapType = Map::class.asClassName()
        val paramType = mapType.parameterizedBy(String::class.asTypeName(), String::class.asTypeName())
        return FunSpec.builder("create").apply {
            addAnnotation(
                AnnotationSpec.builder(PostMapping::class.asClassName()).addMember("\"/$modelPluralSnakeName\"").build()
            )
            addParameter(modelCamelName, paramType)
        }.build()
    }

    private fun getReadFunSpec(): FunSpec {
        return FunSpec.builder("show").apply {
            addAnnotation(
                AnnotationSpec.builder(
                    GetMapping::class.asClassName()
                ).addMember("\"/$modelPluralSnakeName/{id}\"").build()
            )
            addParameter(
                ParameterSpec.builder("id", Long::class).apply {
                    addAnnotation(PathVariable::class.asClassName())
                }.build()
            )
            returns(ModelAndView::class)
            addStatement("val modelAndView = ModelAndView().apply { viewName = \"show\" }")
            addStatement("val $modelCamelName = $repositoryAttributeName.findByIdOrNull(id)")
            addStatement("modelAndView.addObject(\"$modelCamelName\", $modelCamelName)")
            addStatement("return modelAndView")
        }.build()
    }

    private fun getIndexFunSpec(): FunSpec {
        return FunSpec.builder("show").apply {
            addAnnotation(
                AnnotationSpec.builder(
                    GetMapping::class.asClassName()
                ).addMember("\"/$modelPluralSnakeName\"").build()
            )
            returns(ModelAndView::class)
            addStatement("val modelAndView = ModelAndView().apply { viewName = \"index\" }")
            addStatement("val $modelPluralName = $repositoryAttributeName.findAll()")
            addStatement("modelAndView.addObject(\"$modelPluralName\", $modelPluralName)")
            addStatement("return modelAndView")
        }.build()
    }
//
//    private fun getUpdateFunSpec(): FunSpec {
//
//    }
//
//    private fun getDeleteFunSpec(): FunSpec {
//
//    }
}