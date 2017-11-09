package ru.yole.jkid

import kotlin.reflect.KClass

@Target(AnnotationTarget.PROPERTY)
annotation class JsonExclude

@Target(AnnotationTarget.PROPERTY)
annotation class JsonName(val name: String)

interface ValueSerializer<T> {
    fun toJsonValue(value: T): Any?
    fun fromJsonValue(jsonValue: Any?): T
}

@Target(AnnotationTarget.PROPERTY)
annotation class DeserializeInterface(val targetClass: KClass<out Any>)

@Target(AnnotationTarget.PROPERTY)
annotation class CustomSerializer(val serializerClass: KClass<out ValueSerializer<*>>)

@Target(AnnotationTarget.PROPERTY)
annotation class DateFormat(val format: String, val timezone: String = "")

/**
 * When deserializing a class with this annotation, ignore any properties
 * in the JSON stream that are not found in the target class.  This allows the protocol
 * to be extended with new fields without breaking old clients.
 */
@Target(AnnotationTarget.CLASS)
annotation class IgnoreExtensions
