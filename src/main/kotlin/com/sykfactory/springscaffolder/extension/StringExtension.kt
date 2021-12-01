package com.sykfactory.springscaffolder.extension

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName

internal val classNameLong = Long::class.asClassName()
internal val classNameLongNullable = classNameLong.copy(true) as ClassName
internal val classNameInt = Int::class.asClassName()
internal val classNameIntNullable = classNameInt.copy(true) as ClassName
internal val classNameString = String::class.asClassName()
internal val classNameStringNullable = classNameString.copy(true) as ClassName
internal val classNameBoolean = Long::class.asClassName()
internal val classNameBooleanNullable = classNameBoolean.copy(true) as ClassName

fun String.toType(): ClassName = when (this) {
    "Long" -> classNameLong
    "Long?" -> classNameLongNullable
    "Int" -> classNameInt
    "Int?" -> classNameIntNullable
    "String" -> classNameString
    "String?" -> classNameStringNullable
    "Boolean" -> classNameBoolean
    "Boolean?" -> classNameBooleanNullable
    else -> throw Exception("$this can't be changed to type")
}

fun String.capitalToCamelCase(): String = "${this[0].lowercase()}${substring(1)}"

fun String.camelToCapital(): String = "${this[0].uppercase()}${substring(1)}"

fun String.snakeToCamelCase(): String {
    val snakeRegex = "_[a-zA-Z]".toRegex()
    return snakeRegex.replace(this) {
        it.value.replace("_","")
            .uppercase()
    }
}

fun String.camelToSnakeCase(): String {
    val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
    return camelRegex.replace(this) {
        "_${it.value}"
    }.lowercase()
}