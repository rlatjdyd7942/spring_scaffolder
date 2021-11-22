package com.sykfactory.springscaffolder.extension

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName

fun ClassName.defaultValue(): String {
    return if (this.isNullable) {
        "null"
    } else {
        when (this.simpleName) {
            "Long", "Int" ->"0"
            "String" -> "\"\""
            "Boolean" -> "false"
            else -> throw Exception("$this's default value is not defined")
        }
    }
}
