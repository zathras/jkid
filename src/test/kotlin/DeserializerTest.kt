package ru.yole.jkid

import org.junit.Test
import java.io.StringReader
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DeserializerTest {
    @Test fun testSimple() {
        val result = deserialize<SingleStringProp>(StringReader("""{"s": "x"}"""))
        assertEquals("x", result.s)
    }

    @Test fun testObject() {
        val result = deserialize<SingleObjectProp>(StringReader("{\"o\": {\"s\": \"x\"}}"))
        assertEquals("x", result.o.s)
    }

    @Test fun testArray() {
        val result = deserialize<SingleListProp>(StringReader("""{"o": ["a", "b"]}"""))
        assertEquals(2, result.o.size)
        assertEquals("b", result.o[1])
    }

    @Test fun testObjectArray() {
        val result = deserialize<SingleObjectListProp>(StringReader("""{"o": [{"s": "a"}, {"s": "b"}]}"""))
        assertEquals(2, result.o.size)
        assertEquals("b", result.o[1].s)
    }

    @Test fun testOptionalArg() {
        val result = deserialize<SingleOptionalProp>(StringReader("{}"))
        assertEquals("foo", result.s)
    }

    @Test fun testJsonName() {
        val result = deserialize<SingleAnnotatedStringProp>(StringReader("""{"q": "x"}"""))
        assertEquals("x", result.s)
    }

    @Test fun testCustomDeserializer() {
        val result = deserialize<SingleCustomSerializedProp>(StringReader("""{"x": "ONE"}"""))
        assertEquals(1, result.x)
    }

    @Test fun testPropertyTypeMismatch() {
        assertFailsWith<SchemaMismatchException> {
            deserialize<SingleStringProp>(StringReader("{\"s\": 1}"))
        }
    }

    @Test fun testPropertyTypeMismatchNull() {
        assertFailsWith<SchemaMismatchException> {
            deserialize<SingleStringProp>(StringReader("{\"s\": null}"))
        }
    }

    @Test fun testMissingPropertyException() {
        assertFailsWith<SchemaMismatchException> {
            deserialize<SingleStringProp>(StringReader("{}"))
        }
    }

    data class SingleStringProp(val s: String)

    data class SingleObjectProp(val o: SingleStringProp)

    data class SingleListProp(val o: List<String>)

    data class SingleObjectListProp(val o: List<SingleStringProp>)

    data class SingleOptionalProp(val s: String = "foo")

    data class SingleAnnotatedStringProp(@JsonName("q") val s: String)

    class NumberSerializer: ValueSerializer<Int> {
        override fun deserializeValue(jsonValue: Any?): Int = when(jsonValue) {
            "ZERO" -> 0
            "ONE" -> 1
            else -> throw SchemaMismatchException("Unexpected value $jsonValue")
        }

        override fun serializeValue(value: Int): Any? = when(value) {
            0 -> "ZERO"
            1 -> "ONE"
            else -> "?"
        }
    }

    data class SingleCustomSerializedProp(@JsonSerializer(NumberSerializer::class) val x: Int)
}