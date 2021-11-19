package com.sykfactory.springscaffolder.generator.model

data class ModelArguments(
    val tableName: String? = null,
    val modelAttributeArguments: List<ModelAttributeArgument> = listOf()
)
