package com.sykfactory.springscaffolder.generator.model

import com.squareup.kotlinpoet.*
import com.sykfactory.springscaffolder.generator.ClassFileGenerator
import com.sykfactory.springscaffolder.generator.FileGenerator
import com.sykfactory.springscaffolder.generator.FileGenerator.Companion.baseKotlinPath
import com.sykfactory.springscaffolder.util.createFileOnce
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.io.File
import java.lang.String.join
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import kotlin.io.path.Path

class ModelFileGenerator(
    override val packageName: String,
    override val className: String,
    private val modelArguments: ModelArguments
): ClassFileGenerator(packageName, className) {
    override fun createFile() {
        var attributeBuilder = FunSpec.constructorBuilder().apply {
            addParameter(
                ParameterSpec.builder("id", Long::class).defaultValue("0").build()
            )
            addParameters(
                modelArguments.modelAttributeArguments.map {
                    ParameterSpec.builder(it.name, it.type).apply {
                        if (it.defaultValue != null) defaultValue(it.defaultValue)
                    }.build()
                }
            )
        }
        var classTypeSpec = TypeSpec.classBuilder(className).apply {
            addAnnotation(Entity::class.asClassName())
            addAnnotation(
                AnnotationSpec.builder(Table::class.asClassName()).apply {
                    if (modelArguments.tableName != null) addMember("name = %S", modelArguments.tableName)
                }.build()
            )
            addModifiers(KModifier.DATA)
            primaryConstructor(attributeBuilder.build())
            addProperty(
                PropertySpec.builder("id", Long::class.java).apply {
                    addAnnotation(Id::class.asClassName())
                    addAnnotation(GeneratedValue::class.asClassName())
                    initializer("id")
                    mutable()
                }.build()
            )
            addProperties(
                modelArguments.modelAttributeArguments.map {
                    PropertySpec.builder(it.name, it.type).initializer(it.name).mutable().build()
                }
            )
            addProperty(
                PropertySpec.builder("createdAt", LocalDateTime::class.java).apply {
                    addAnnotation(CreationTimestamp::class.asClassName())
                    initializer("%T.now()", LocalDateTime::class)
                    mutable()
                }.build()
            )
            addProperty(
                PropertySpec.builder("updatedAt", LocalDateTime::class.java).apply {
                    addAnnotation(UpdateTimestamp::class.asClassName())
                    initializer("%T.now()", LocalDateTime::class)
                    mutable()
                }.build()
            )
        }.build()
        FileSpec.builder(packageName, className).apply {
            addType(classTypeSpec)
        }.build().writeTo(Path(baseKotlinPath))
    }
}