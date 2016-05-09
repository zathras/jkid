package ru.yole.jkid

import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KProperty
import kotlin.reflect.memberProperties
import kotlin.reflect.primaryConstructor

inline fun <reified T> KAnnotatedElement.findAnnotation(): T? = annotations.filterIsInstance<T>().firstOrNull()

private fun StringBuilder.serializeString(s: String) {
    append('\"')
    append(s)  // TODO escape special characters
    append('\"')
}

private fun StringBuilder.serializePropertyValue(value: Any?) = when(value) {
    null -> append("null")
    is String -> serializeString(value)
    is Number, is Boolean -> append(value.toString())
    is List<*> -> serializeArray(value as List<Any>)
    else -> serializeObject(value)
}

private fun StringBuilder.serializeProperty(prop: KProperty<Any?>, value: Any?) {
    serializeString(prop.findAnnotation<JsonName>()?.value ?: prop.name)
    append(": ")

    val jsonSerializer = prop.findAnnotation<JsonSerializer>()
    if (jsonSerializer != null) {
        val primaryConstructor = jsonSerializer.serializerClass.primaryConstructor
                ?: throw IllegalArgumentException("Class specified as @JsonSerializer must have a no-arg primary constructor")
        val valueSerializer = primaryConstructor.call() as ValueSerializer<Any?>
        serializePropertyValue(valueSerializer.serializeValue(value))
    }
    else {
        serializePropertyValue(value)
    }
}

private fun StringBuilder.serializeObject(x: Any) {
    append("{")
    for ((i, prop) in x.javaClass.kotlin.memberProperties.withIndex()) {
        if (i > 0) append(", ")
        serializeProperty(prop, prop.get(x))
    }
    append("}")
}

private fun StringBuilder.serializeArray(data: List<Any>) {
    append("[")
    for ((i, x) in data.withIndex()) {
        if (i > 0) append(", ")
        serializePropertyValue(x)
    }
    append("]")

}

fun serialize(x: Any): String {
    val result = StringBuilder()
    result.serializeObject(x)
    return result.toString()
}
