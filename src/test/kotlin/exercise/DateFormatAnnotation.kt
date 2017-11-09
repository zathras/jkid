package ru.yole.jkid

import org.junit.Ignore
import org.junit.Test
import ru.yole.jkid.DateFormat
import ru.yole.jkid.deserialization.deserialize
import ru.yole.jkid.serialization.serialize
import java.text.SimpleDateFormat
import java.util.*
import kotlin.test.assertEquals

data class Person(
        val name: String,
        @DateFormat("dd-MM-yyyy") val birthDate: Date
)

class DateFormatTest {
    private val value = Person("Alice", SimpleDateFormat("dd-MM-yyyy").parse("13-02-1987"))
    private val json = """{"birthDate": "13-02-1987", "name": "Alice"}"""

    @Test fun testSerialization() {
        assertEquals(json, serialize(value))
    }

    @Test fun testDeserialization() {
        assertEquals(value, deserialize(json))
    }
}

data class TimeWithZone(
    @DateFormat("yyyy-MM-dd HH:mm:ss.SSS zzz", "GMT") val testDate: Date
)

class DateFormatTestWithTimezone {
    private val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS zzz")
    private val value = TimeWithZone(df.parse("2017-08-18 17:25:26.632 PDT"))
    private val json = """{"testDate": "2017-08-19 00:25:26.632 GMT"}"""

    @Test fun testSerialization() {
        assertEquals(json, serialize(value))
    }

    @Test fun testDeserialization() {
        assertEquals(value, deserialize(json))
    }
}
