package com.sykfactory.springscaffolder.generator.model

import com.squareup.kotlinpoet.*
import com.sykfactory.springscaffolder.generator.ClassFileGenerator
import com.sykfactory.springscaffolder.generator.FileGenerator
import com.sykfactory.springscaffolder.generator.FileGenerator.Companion.baseKotlinPath
import com.sykfactory.springscaffolder.util.createFileOnce
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GeneratorType
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.File
import java.lang.String.join
import java.time.LocalDateTime
import javax.persistence.*
import kotlin.io.path.Path

class ModelFileGenerator(
    override val className: ClassName,
    private val modelArguments: ModelArguments
): ClassFileGenerator(className) {
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
        var classTypeSpec = TypeSpec.classBuilder(className.simpleName).apply {
            addAnnotation(Entity::class.asClassName())
            addAnnotation(
                AnnotationSpec.builder(Table::class.asClassName()).apply {
                    if (modelArguments.tableName != null) addMember("name = %S", modelArguments.tableName)
                }.build()
            )
            addAnnotation(
                AnnotationSpec.builder(EntityListeners::class.asClassName()).apply {
                    addMember("%T::class", AuditingEntityListener::class)
                }.build()
            )
            addModifiers(KModifier.DATA)
            primaryConstructor(attributeBuilder.build())
            addProperty(
                PropertySpec.builder("id", Long::class.java).apply {
                    addAnnotation(Id::class.asClassName())
                    addAnnotation(
                        AnnotationSpec.builder(GeneratedValue::class.asClassName()).apply {
                            addMember("strategy = %T.IDENTITY", GenerationType::class.asClassName())
                        }.build()
                    )
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
                    addAnnotation(CreatedDate::class.asClassName())
                    addAnnotation(
                        AnnotationSpec.builder(Column::class.asClassName()).apply {
                            addMember("updatable = false")
                        }.build()
                    )
                    initializer("%T.now()", LocalDateTime::class)
                    mutable()
                }.build()
            )
            addProperty(
                PropertySpec.builder("updatedAt", LocalDateTime::class.java).apply {
                    addAnnotation(LastModifiedDate::class.asClassName())
                    initializer("%T.now()", LocalDateTime::class)
                    mutable()
                }.build()
            )
        }.build()
        FileSpec.builder(className.packageName, className.simpleName).apply {
            addType(classTypeSpec)
        }.build().writeTo(Path(baseKotlinPath))
    }
}