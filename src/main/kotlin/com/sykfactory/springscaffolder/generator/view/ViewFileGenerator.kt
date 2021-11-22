package com.sykfactory.springscaffolder.generator.view

import capitalToCamelCase
import com.sykfactory.springscaffolder.builder.HtmlBuilder
import com.sykfactory.springscaffolder.generator.FileGenerator
import com.sykfactory.springscaffolder.generator.FileGenerator.Companion.baseTemplatePath
import com.sykfactory.springscaffolder.generator.model.ModelArguments
import com.sykfactory.springscaffolder.util.createFileOnce
import camelToSnakeCase
import java.lang.String.join

class ViewFileGenerator(
    private val detailedControllerPackagePath: String,
    private val modelClassName: String,
    private val modelArguments: ModelArguments
) : FileGenerator {
    private val modelCamelName = modelClassName.capitalToCamelCase()
    private val modelPluralSnakeName = "${modelCamelName}s".camelToSnakeCase()
    private val viewModelTemplatePath = run {
        if (detailedControllerPackagePath == "") {
            modelPluralSnakeName
        } else {
            val prefix = detailedControllerPackagePath.replace(".", "/")
            join("/", prefix, modelPluralSnakeName).replace("//", "/")
        }
    }


    override fun generateFile() {
        createModelContentFragmentFile()
        createNewFile()
        createShowFile()
        createIndexFile()
        createEditFile()
    }

    private fun createIndexFile() {
        val html = HtmlBuilder().html {
            head {
                title {
                    multiLine = false
                    text("${modelClassName}s")
                }
            }
            body {
                h2 {
                    multiLine = false
                    text("${modelClassName}s")
                }
                table {
                    tr {
                        th {
                            multiLine = false
                            text("id")
                        }
                        modelArguments.modelAttributeArguments.forEach {
                            th {
                                multiLine = false
                                text(it.name)
                            }
                        }
                        th {
                            multiLine = false
                            text("createdAt")
                        }
                        th {
                            multiLine = false
                            text("updatedAt")
                        }
                        th {
                            multiLine = false
                        }
                        th {
                            multiLine = false
                        }
                        th {
                            multiLine = false
                        }
                    }
                    tr {
                        attribute("th:each", "$modelCamelName : \${${modelCamelName}s}")
                        td {
                            multiLine = false
                            attribute("th:text", "\${$modelCamelName.id}")
                        }
                        modelArguments.modelAttributeArguments.forEach {
                            td {
                                multiLine = false
                                attribute("th:text", "\${$modelCamelName.${it.name}}")
                            }
                        }
                        td {
                            multiLine = false
                            attribute("th:text", "\${$modelCamelName.createdAt}")
                        }
                        td {
                            multiLine = false
                            attribute("th:text", "\${$modelCamelName.updatedAt}")
                        }
                        td {
                            form {
                                attribute("th:action", "@{$modelPluralSnakeName/{id}(id=\${$modelCamelName.id})}")
                                attribute("method", "get")
                                input {
                                    attribute("type", "submit")
                                    attribute("value", "show")
                                }
                            }
                        }
                        td {
                            form {
                                attribute("th:action", "@{$modelPluralSnakeName/{id}/edit(id=\${$modelCamelName.id})}")
                                attribute("method", "get")
                                input {
                                    attribute("type", "submit")
                                    attribute("value", "edit")
                                }
                            }
                        }
                        td {
                            form {
                                attribute("th:action", "@{$modelPluralSnakeName/{id}/delete(id=\${$modelCamelName.id})}")
                                attribute("method", "post")
                                input {
                                    attribute("type", "submit")
                                    attribute("value", "delete")
                                }
                            }
                        }
                    }
                }
                form {
                    attribute("action", "/$viewModelTemplatePath/new")
                    attribute("method", "get")
                    input {
                        attribute("type", "submit")
                        attribute("value", "New $modelClassName")
                    }
                }
            }
        }.build()
        createFileOnce(fileFullPath("index.html")).writeText(html)
    }

    private fun createModelContentFragmentFile() {
        val html = HtmlBuilder().html {
            div {
                attribute("th:fragment", "_model_form_content")
                input {
                    attribute("type", "hidden")
                    attribute("id", "id")
                    attribute("name", "id")
                    attribute("th:value", "\${$modelCamelName.id}")
                }
                modelArguments.modelAttributeArguments.forEach {
                    div {
                        label {
                            attribute("for", it.name.camelToSnakeCase())
                            text("${it.name}:")
                        }
                        input {
                            attribute("type", "text")
                            attribute("id", it.name.camelToSnakeCase())
                            attribute("name", it.name.camelToSnakeCase())
                            attribute("th:value", "\${$modelCamelName.${it.name.camelToSnakeCase()}}")
                        }
                    }
                }
                input {
                    attribute("type", "submit")
                    attribute("value", "save")
                }
            }
        }.build()
        createFileOnce(fileFullPath("_model_form_content.html")).writeText(html)
    }

    private fun createNewFile() {
        val html = HtmlBuilder().html {
            head {
                title {
                    multiLine = false
                    text("New $modelClassName")
                }
            }
            body {
                h2 {
                    multiLine = false
                    text("New $modelClassName")
                }
                form {
                    attribute("action", "create")
                    attribute("method", "post")
                    div {
                        multiLine = false
                        attribute("th:replace", "${modelContentPath()} :: _model_form_content")
                    }
                }
            }
        }.build()
        createFileOnce(fileFullPath("new.html")).writeText(html)
    }

    private fun createShowFile() {
        val html = HtmlBuilder().html {
            head {
                title {
                    multiLine = false
                    text("Show ${modelClassName}")
                }
            }
            body {
                h2 {
                    multiLine = false
                    text("Show ${modelClassName}")
                }
                div {
                    label {
                        text("id:")
                    }
                    label {
                        attribute("th:text", "\${$modelCamelName.id}")
                    }
                }
                modelArguments.modelAttributeArguments.forEach {
                    div {
                        label {
                            text("${it.name}:")
                        }
                        label {
                            attribute("th:text", "\${$modelCamelName.${it.name}}")
                        }
                    }
                }
                div {
                    label {
                        text("createdAt:")
                    }
                    label {
                        attribute("th:text", "\${$modelCamelName.createdAt}")
                    }
                }
                div {
                    label {
                        text("updatedAt:")
                    }
                    label {
                        attribute("th:text", "\${$modelCamelName.updatedAt}")
                    }
                }
                td {
                    form {
                        attribute("th:action", "@{{id}/edit(id=\${$modelCamelName.id})}")
                        attribute("method", "get")
                        input {
                            attribute("type", "submit")
                            attribute("value", "Edit $modelClassName")
                        }
                    }
                }
                td {
                    form {
                        attribute("th:action", "@{{id}/delete(id=\${$modelCamelName.id})}")
                        attribute("method", "post")
                        input {
                            attribute("type", "submit")
                            attribute("value", "Delete $modelClassName")
                        }
                    }
                }
                br {}
                a {
                    multiLine = false
                    attribute("href", "/${viewModelTemplatePath}")
                    text("back")
                }
            }
        }.build()
        createFileOnce(fileFullPath("show.html")).writeText(html)
    }

    private fun createEditFile() {
        val html = HtmlBuilder().html {
            head {
                title {
                    multiLine = false
                    text("Edit $modelClassName")
                }
            }
            body {
                h2 {
                    multiLine = false
                    text("Edit $modelClassName")
                }
                form {
                    attribute("action", "update")
                    attribute("method", "post")
                    div {
                        multiLine = false
                        attribute("th:replace", "${modelContentPath()} :: _model_form_content")
                    }
                }
            }
        }.build()
        createFileOnce(fileFullPath("edit.html")).writeText(html)
    }

    private fun modelContentPath(): String
        = join("/", viewModelTemplatePath, "_model_form_content").replace("//", "/")

    private fun fileFullPath(filePath: String): String
        = join("/", baseTemplatePath, viewModelTemplatePath, filePath)
            .replace("//", "/")
}