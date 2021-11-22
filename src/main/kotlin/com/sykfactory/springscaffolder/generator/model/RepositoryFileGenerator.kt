package com.sykfactory.springscaffolder.generator.model

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.sykfactory.springscaffolder.generator.ClassFileGenerator
import com.sykfactory.springscaffolder.generator.FileGenerator.Companion.baseKotlinPath
import org.springframework.data.jpa.repository.JpaRepository
import kotlin.io.path.Path

class RepositoryFileGenerator(
    override val className: ClassName,
    private val classNameModel: ClassName
): ClassFileGenerator(className) {
    override fun createFile() {
        val jpaRepository = JpaRepository::class.asClassName()
        val type = jpaRepository.parameterizedBy(ClassName(classNameModel.packageName, classNameModel.simpleName), Long::class.asTypeName())
        FileSpec.builder(className.packageName, className.simpleName).apply {
            addImport(classNameModel.packageName, classNameModel.simpleName)
            addType(
                TypeSpec.interfaceBuilder(className.simpleName).addSuperinterface(
                    type
                ).build()
            )
        }.build().writeTo(Path(baseKotlinPath))
    }
}