import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName


fun ClassName.defaultValue(): String? {
    return if (this.isNullable) {
        "null"
    } else {
        when (this) {
            Long::class.asClassName(), Int::class.asClassName() ->"0"
            String::class.asClassName() -> "\"\""
            Boolean::class.asClassName() -> "false"
            else -> throw Exception("$this's default value is not defined")
        }
    }
}