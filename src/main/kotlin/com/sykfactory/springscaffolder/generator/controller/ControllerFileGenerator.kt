package com.sykfactory.springscaffolder.generator.controller

import capitalToCamelCase
import com.squareup.kotlinpoet.*
import com.sykfactory.springscaffolder.generator.ClassFileGenerator
import com.sykfactory.springscaffolder.generator.FileGenerator
import com.sykfactory.springscaffolder.generator.model.ModelArguments
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import camelToSnakeCase
import java.lang.String.join
import kotlin.io.path.Path

class ControllerFileGenerator(
    private val detailedControllerPackagePath: String,
    override val className: ClassName,
    private val classNameModel: ClassName,
    private val classNameRepository: ClassName
): ClassFileGenerator(className) {
    private val repositoryCamelName = classNameRepository.simpleName.capitalToCamelCase()
    private val modelCamelName = classNameModel.simpleName.capitalToCamelCase()
    private val modelPluralName = "${modelCamelName}s"
    private val modelPluralSnakeName = modelPluralName.camelToSnakeCase()
    private val controllerRequestPath = run {
        if (detailedControllerPackagePath == "") {
            modelPluralSnakeName
        } else {
            val prefix = detailedControllerPackagePath.replace(".", "/")
            join("/", prefix, modelPluralSnakeName).replace("//", "/")
        }
    }

    override fun createFile() {
        var attributeBuilder = FunSpec.constructorBuilder().apply {
            addParameter(
                ParameterSpec.builder(
                    repositoryCamelName, classNameRepository
                ).build()
            )
        }
        var classTypeSpec = TypeSpec.classBuilder(className.simpleName).apply {
            primaryConstructor(attributeBuilder.build())
            addProperty(
                PropertySpec.builder(
                    repositoryCamelName, classNameRepository, KModifier.PRIVATE
                ).initializer(repositoryCamelName).build()
            )
            addAnnotation(RestController::class.asClassName())
            addAnnotation(
                AnnotationSpec.builder(
                    RequestMapping::class.asClassName()
                ).addMember("\"/$controllerRequestPath\"").build()
            )
            addFunctions(
                listOf(
                    getIndexFunSpec(),
                    getNewFunSpec(),
                    getCreateFunSpec(),
                    getShowFunSpec(),
                    getEditFunSpec(),
                    getUpdateFunSpec(),
                    getDeleteFunSpec()
                )
            )
        }.build()
        FileSpec.builder(className.packageName, className.simpleName).apply {
            addImport("org.springframework.data.repository", "findByIdOrNull")
            addImport(ModelAndView::class, listOf())
            addImport(classNameModel.packageName, classNameModel.simpleName)
            addType(classTypeSpec)
        }.build().writeTo(Path(FileGenerator.baseKotlinPath))
    }

    private fun getNewFunSpec(): FunSpec {
        return FunSpec.builder("new").apply {
            addAnnotation(
                AnnotationSpec.builder(
                    GetMapping::class.asClassName()
                ).addMember("\"/new\"").build()
            )
            returns(ModelAndView::class)
            addStatement("val modelAndView = ModelAndView(\"$controllerRequestPath/new\")")
            addStatement("modelAndView.addObject(\"$modelCamelName\", ${classNameModel.simpleName}())")
            addStatement("return modelAndView")
        }.build()
    }

    private fun getCreateFunSpec(): FunSpec {
        return FunSpec.builder("create").apply {
            addAnnotation(
                AnnotationSpec.builder(PostMapping::class.asClassName()).addMember("\"/create\"").build()
            )
            returns(ModelAndView::class)
            addParameter(modelCamelName, classNameModel)
            addStatement("$repositoryCamelName.save($modelCamelName)")
            addStatement("return ModelAndView(\"redirect:/$controllerRequestPath\")")
        }.build()
    }

    private fun getShowFunSpec(): FunSpec {
        return FunSpec.builder("show").apply {
            addAnnotation(
                AnnotationSpec.builder(
                    GetMapping::class.asClassName()
                ).addMember("\"/{id}\"").build()
            )
            addParameter(
                ParameterSpec.builder("id", Long::class).apply {
                    addAnnotation(PathVariable::class.asClassName())
                }.build()
            )
            returns(ModelAndView::class)
            addStatement("val modelAndView = ModelAndView(\"$controllerRequestPath/show\")")
            addStatement("val $modelCamelName = $repositoryCamelName.findByIdOrNull(id)")
            addStatement("modelAndView.addObject(\"$modelCamelName\", $modelCamelName)")
            addStatement("return modelAndView")
        }.build()
    }

    private fun getIndexFunSpec(): FunSpec {
        return FunSpec.builder("index").apply {
            addAnnotation(
                AnnotationSpec.builder(
                    GetMapping::class.asClassName()
                ).build()
            )
            returns(ModelAndView::class)
            addStatement("val modelAndView = ModelAndView(\"$controllerRequestPath/index\")")
            addStatement("val $modelPluralName = $repositoryCamelName.findAll()")
            addStatement("modelAndView.addObject(\"$modelPluralName\", $modelPluralName)")
            addStatement("return modelAndView")
        }.build()
    }

    private fun getEditFunSpec(): FunSpec {
        return FunSpec.builder("edit").apply {
            addAnnotation(
                AnnotationSpec.builder(
                    GetMapping::class.asClassName()
                ).addMember("\"/{id}/edit\"").build()
            )
            addParameter(
                ParameterSpec.builder("id", Long::class).apply {
                    addAnnotation(PathVariable::class.asClassName())
                }.build()
            )
            returns(ModelAndView::class)
            addStatement("val modelAndView = ModelAndView(\"$controllerRequestPath/edit\")")
            addStatement("modelAndView.addObject(\"$modelCamelName\", $repositoryCamelName.findByIdOrNull(id))")
            addStatement("return modelAndView")
        }.build()
    }

    private fun getUpdateFunSpec(): FunSpec {
        return FunSpec.builder("update").apply {
            addAnnotation(
                AnnotationSpec.builder(PostMapping::class.asClassName()).addMember("\"/{id}/update\"").build()
            )
            returns(ModelAndView::class)
            addParameter(modelCamelName, classNameModel)
            addStatement("$repositoryCamelName.save($modelCamelName)")
            addStatement("return ModelAndView(\"redirect:/$controllerRequestPath\")")
        }.build()
    }

    private fun getDeleteFunSpec(): FunSpec {
        return FunSpec.builder("delete").apply {
            addAnnotation(
                AnnotationSpec.builder(
                    PostMapping::class.asClassName()
                ).addMember("\"/{id}/delete\"").build()
            )
            addParameter(
                ParameterSpec.builder("id", Long::class).apply {
                    addAnnotation(PathVariable::class.asClassName())
                }.build()
            )
            returns(ModelAndView::class)
            addStatement("$repositoryCamelName.deleteById(id)")
            addStatement("return ModelAndView(\"redirect:/$controllerRequestPath\")")
        }.build()
    }
}