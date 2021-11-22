import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName

fun String.toType(): ClassName = when (this) {
    "Long" -> Long::class.asClassName()
    "Long?" -> Long::class.asClassName().copy(true) as ClassName
    "Int" -> Int::class.asClassName()
    "Int?" -> Int::class.asClassName().copy(true) as ClassName
    "String" -> String::class.asClassName()
    "String?" -> String::class.asClassName().copy(true) as ClassName
    "Boolean" -> Boolean::class.asClassName()
    "Boolean?" -> Boolean::class.asClassName().copy(true) as ClassName
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