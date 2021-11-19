package com.sykfactory.springscaffolder.generator.model

import com.squareup.kotlinpoet.ClassName
import defaultValue
import toType

data class ModelAttributeArgument(
    val name: String,
    val type: ClassName,
    val defaultValue: String?
) {
    companion object {
        fun parse(arg: String): ModelAttributeArgument {
            val arr = arg.split('=')
            val nameAndType = arr[0].split(':')
            val type = nameAndType[1].toType()
            val defaultValue = if (arr.size == 1) {
                type.defaultValue()
            } else {
                "\"${arr[1]}\""
            }
            return ModelAttributeArgument(nameAndType[0], type, defaultValue)
        }
    }
}