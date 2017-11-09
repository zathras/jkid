package deserialization

import org.junit.Test
import ru.yole.jkid.*
import ru.yole.jkid.deserialization.deserialize
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

@IgnoreExtensions
data class ExtensibleData (
    val onlyProperty : String
)

class DeserializerTest {
    @Test
    fun testSimple() {
        val json = """{"onlyProperty" : "foo", "extension" : "bar"}"""
        val result = deserialize<ExtensibleData>(json)
        assertEquals("foo", result.onlyProperty)
    }

    @Test
    fun testObject() {
        val json = """{"onlyProperty" : "foo", "extension" : { "glorp" : "bar" } }"""
        val result = deserialize<ExtensibleData>(json)
        assertEquals("foo", result.onlyProperty)
    }

    @Test
    fun testArray() {
        val json = """{"onlyProperty" : "foo", "extension" : [ "bar", "glorp" ] }"""
        val result = deserialize<ExtensibleData>(json)
        assertEquals("foo", result.onlyProperty)
    }

    @Test
    fun testComplexObject() {
        val json = """{"onlyProperty" : "foo", "extension" : { "glorp" : { "glorp2" : "bar", "arr" : [ 3 ] } } }"""
        val result = deserialize<ExtensibleData>(json)
        assertEquals("foo", result.onlyProperty)
    }

}
