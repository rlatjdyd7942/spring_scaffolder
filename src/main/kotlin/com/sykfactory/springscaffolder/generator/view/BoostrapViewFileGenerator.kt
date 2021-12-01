package com.sykfactory.springscaffolder.generator.view

import com.sykfactory.springscaffolder.builder.HtmlBuilder
import com.sykfactory.springscaffolder.extension.camelToSnakeCase
import com.sykfactory.springscaffolder.extension.capitalToCamelCase
import com.sykfactory.springscaffolder.generator.FileGenerator
import com.sykfactory.springscaffolder.generator.model.ModelArguments
import com.sykfactory.springscaffolder.util.createFileOnce

class BootstrapViewFileGenerator(
    private val detailedControllerPackagePath: String,
    private val modelClassName: String,
    private val modelArguments: ModelArguments,
    private val layoutPath: String
) : FileGenerator {
    private val modelCamelName = modelClassName.capitalToCamelCase()
    private val modelPluralSnakeName = "${modelCamelName}s".camelToSnakeCase()
    private val viewModelTemplatePath = run {
        if (detailedControllerPackagePath == "") {
            modelPluralSnakeName
        } else {
            val prefix = detailedControllerPackagePath.replace(".", "/")
            java.lang.String.join("/", prefix, modelPluralSnakeName).replace("//", "/")
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
            attribute("layout:decorate", layoutPath)
            div {
                attribute("layout:fragment", "_page_content")
                h2 {
                    multiLine = false
                    text("${modelClassName}s")
                }
                table {
                    classes("table table-hover table-striped")
                    thead {
                        tr {
                            th {
                                multiLine = false
                                attribute("scope", "col")
                                text("id")
                            }
                            modelArguments.modelAttributeArguments.forEach {
                                th {
                                    multiLine = false
                                    classes("text-center")
                                    attribute("scope", "col")
                                    text(it.name)
                                }
                            }
                            th {
                                multiLine = false
                                classes("text-center")
                                attribute("scope", "col")
                                text("createdAt")
                            }
                            th {
                                multiLine = false
                                classes("text-center")
                                attribute("scope", "col")
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
                    }
                    tbody {
                        tr {
                            attribute("th:each", "$modelCamelName : \${${modelCamelName}s}")
                            th {
                                multiLine = false
                                classes("align-middle")
                                attribute("scope", "row")
                                attribute("th:text", "\${$modelCamelName.id}")
                            }
                            modelArguments.modelAttributeArguments.forEach {
                                td {
                                    multiLine = false
                                    classes("align-middle")
                                    attribute("th:text", "\${$modelCamelName.${it.name}}")
                                }
                            }
                            td {
                                multiLine = false
                                classes("align-middle")
                                attribute("th:text", "\${$modelCamelName.createdAt}")
                            }
                            td {
                                multiLine = false
                                classes("align-middle")
                                attribute("th:text", "\${$modelCamelName.updatedAt}")
                            }
                            td {
                                classes("align-middle")
                                form {
                                    attribute("th:action", "@{$modelPluralSnakeName/{id}(id=\${$modelCamelName.id})}")
                                    attribute("method", "get")
                                    input {
                                        classes("btn btn-link")
                                        attribute("type", "submit")
                                        attribute("value", "show")
                                    }
                                }
                            }
                            td {
                                classes("align-middle")
                                form {
                                    attribute("th:action", "@{$modelPluralSnakeName/{id}/edit(id=\${$modelCamelName.id})}")
                                    attribute("method", "get")
                                    input {
                                        classes("btn btn-success")
                                        attribute("type", "submit")
                                        attribute("value", "edit")
                                    }
                                }
                            }
                            td {
                                classes("align-middle")
                                form {
                                    attribute("th:action", "@{$modelPluralSnakeName/{id}/delete(id=\${$modelCamelName.id})}")
                                    attribute("method", "post")
                                    input {
                                        classes("btn btn-danger")
                                        attribute("type", "submit")
                                        attribute("value", "delete")
                                    }
                                }
                            }
                        }
                    }
                }
                form {
                    attribute("action", "/$viewModelTemplatePath/new")
                    attribute("method", "get")
                    input {
                        classes("btn btn-primary")
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
                        classes("row mb-3")
                        label {
                            classes("col-sm-2 col-form-label")
                            attribute("for", it.name.camelToSnakeCase())
                            text(it.name)
                        }
                        div {
                            classes("col-auto")
                            input {
                                classes("form-control")
                                attribute("type", "text")
                                attribute("id", it.name.camelToSnakeCase())
                                attribute("name", it.name.camelToSnakeCase())
                                attribute("th:value", "\${$modelCamelName.${it.name.camelToSnakeCase()}}")
                            }
                        }
                    }
                }
                input {
                    classes("btn btn-primary")
                    attribute("type", "submit")
                    attribute("value", "save")
                }
            }
        }.build()
        createFileOnce(fileFullPath("_model_form_content.html")).writeText(html)
    }

    private fun createNewFile() {
        val html = HtmlBuilder().html {
            attribute("layout:decorate", layoutPath)
            div {
                attribute("layout:fragment", "_page_content")
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
            attribute("layout:decorate", layoutPath)
            div {
                attribute("layout:fragment", "_page_content")
                h2 {
                    multiLine = false
                    text("Show ${modelClassName}")
                }
                div {
                    classes("row mb-3")
                    label {
                        classes("col-sm-2 col-form-label")
                        attribute("for", "id")
                        text("id")
                    }
                    div {
                        classes("col-auto")
                        input {
                            classes("form-control")
                            state("readonly")
                            attribute("type", "text")
                            attribute("id", "id")
                            attribute("name", "id")
                            attribute("th:value", "\${$modelCamelName.id}")
                        }
                    }
                }
                modelArguments.modelAttributeArguments.forEach {
                    div {
                        classes("row mb-3")
                        label {
                            classes("col-sm-2 col-form-label")
                            attribute("for", it.name)
                            text(it.name)
                        }
                        div {
                            classes("col-auto")
                            input {
                                classes("form-control")
                                state("readonly")
                                attribute("type", "text")
                                attribute("id", it.name)
                                attribute("name", it.name)
                                attribute("th:value", "\${$modelCamelName.${it.name}}")
                            }
                        }
                    }
                }
                div {
                    classes("row mb-3")
                    label {
                        classes("col-sm-2 col-form-label")
                        attribute("for", "createdAt")
                        text("createdAt")
                    }
                    div {
                        classes("col-auto")
                        input {
                            classes("form-control")
                            state("readonly")
                            attribute("type", "text")
                            attribute("id", "createdAt")
                            attribute("name", "createdAt")
                            attribute("th:value", "\${$modelCamelName.createdAt}")
                        }
                    }
                }
                div {
                    classes("row mb-3")
                    label {
                        classes("col-sm-2 col-form-label")
                        attribute("for", "updatedAt")
                        text("updatedAt")
                    }
                    div {
                        classes("col-auto")
                        input {
                            classes("form-control")
                            state("readonly")
                            attribute("type", "text")
                            attribute("id", "updatedAt")
                            attribute("name", "updatedAt")
                            attribute("th:value", "\${$modelCamelName.updatedAt}")
                        }
                    }
                }
                div {
                    classes("row my-3")
                    form {
                        classes("col-auto")
                        attribute("th:action", "@{{id}/edit(id=\${$modelCamelName.id})}")
                        attribute("method", "get")
                        input {
                            classes("btn btn-success")
                            attribute("type", "submit")
                            attribute("value", "Edit $modelClassName")
                        }
                    }
                    form {
                        classes("col-auto")
                        attribute("th:action", "@{{id}/delete(id=\${$modelCamelName.id})}")
                        attribute("method", "post")
                        input {
                            classes("btn btn-danger")
                            attribute("type", "submit")
                            attribute("value", "Delete $modelClassName")
                        }
                    }
                }
                a {
                    multiLine = false
                    classes("btn btn-link")
                    attribute("href", "/${viewModelTemplatePath}")
                    text("back")
                }
            }
        }.build()
        createFileOnce(fileFullPath("show.html")).writeText(html)
    }

    private fun createEditFile() {
        val html = HtmlBuilder().html {
            attribute("layout:decorate", layoutPath)
            div {
                attribute("layout:fragment", "_page_content")
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
        = java.lang.String.join("/", viewModelTemplatePath, "_model_form_content").replace("//", "/")

    private fun fileFullPath(filePath: String): String
        = java.lang.String.join("/", FileGenerator.baseTemplatePath, viewModelTemplatePath, filePath)
        .replace("//", "/")
}
