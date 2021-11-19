package com.sykfactory.springscaffolder.generator.model

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.sykfactory.springscaffolder.generator.ClassFileGenerator
import com.sykfactory.springscaffolder.generator.FileGenerator.Companion.baseKotlinPath
import org.springframework.data.jpa.repository.JpaRepository
import kotlin.io.path.Path

class RepositoryFileGenerator(
    override val packageName: String,
    override val className: String,
    private val modelPackageName: String,
    private val modelClassName: String
): ClassFileGenerator(packageName, className) {
    override fun createFile() {
        val jpaRepository = JpaRepository::class.asClassName()
        val type = jpaRepository.parameterizedBy(ClassName(modelPackageName, modelClassName), Long::class.asTypeName())
        FileSpec.builder(packageName, className).apply {
            addImport(modelPackageName, modelClassName)
            addType(
                TypeSpec.interfaceBuilder(className).addSuperinterface(
                    type
                ).build()
            )
        }.build().writeTo(Path(baseKotlinPath))
    }

}