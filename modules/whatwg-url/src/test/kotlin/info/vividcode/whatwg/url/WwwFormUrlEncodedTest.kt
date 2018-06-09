package info.vividcode.whatwg.url

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class WwwFormUrlEncodedTest {

    @Test
    fun parseWwwFormUrlEncoded() {
        val testInput = "ab%c=ddd+%3+0%39aaa&efg=klm".toByteArray()
        val expected = listOf(
            "ab%c" to "ddd %3 09aaa",
            "efg" to "klm"
        )

        val actual = parseWwwFormUrlEncoded(testInput)
        assertEquals(expected, actual)
    }

    @Test
    fun parseWwwFormUrlEncoded_emptyByteSequence() {
        val testInput = byteArrayOf()
        val expected = emptyList<Pair<String, String>>()

        val actual = parseWwwFormUrlEncoded(testInput)
        assertEquals(expected, actual)
    }

    @Test
    fun percentDecode() {
        val testValue = "abc%39%"
        val expected = "abc9%"

        val actual = percentDecode(testValue.toByteArray())
        assertArrayEquals(expected.toByteArray(), actual)
    }

}
