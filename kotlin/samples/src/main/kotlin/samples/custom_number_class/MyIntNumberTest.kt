package samples.custom_number_class

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

/**
 * API test class for [MyIntNumber] class
 */
internal class MyIntNumberTest {

    @Test
    fun test01_AddOperator() {
        println("test01_AddOperator() started")

        val number1 = MyIntNumber(1)
        val number2 = MyIntNumber(2)

        val actualResult = number1 + number2

        assertEquals(MyIntNumber(3), actualResult)

        var number3 = MyIntNumber(0)
        number3 += number2

        assertEquals(MyIntNumber(2), number3)

        println("test01_AddOperator() finished")
    }

    @Test
    fun test02_EqualOperator() {
        println("test02_EqualOperator() started")

        val number1 = MyIntNumber(1)
        val number3 = number1

        // 1. References are equal
        assertEquals(MyIntNumber(1), number3)

        // 2. Types are not equal
        assertNotEquals(number1, "")

        // 3. Types are equals, values are not
        assertNotEquals(MyIntNumber(2), number1)

        // 3. Types and values are equal
        assertEquals(MyIntNumber(100), MyIntNumber(100))

        println("test02_EqualOperator() finished")
    }

    @Test
    fun test03_RangeOperator() {
        println("test03_RangeOperator() started")

        val range1 = MyIntNumber(1)..MyIntNumber(100)

        // Check bounds
        assertEquals(range1.start, MyIntNumber(1))
        assertEquals(range1.endInclusive, MyIntNumber(100))

        // Check if value is in range
        var result = range1.contains(MyIntNumber(50))
        assertTrue(result)
        result = range1.contains(MyIntNumber(0))
        assertFalse(result)

        // Check if it is empty
        result = range1.isEmpty()
        assertFalse(result)

        println("test03_RangeOperator() finished")
    }

    @Test
    fun test04_IteratorOperator() {
        println("test04_IteratorOperator() started")

        val range1 = MyIntNumber(1)..MyIntNumber(10)
        val intRange = 1..10

        println("test04_IteratorOperator() *********************")

        val iterator = range1.iterator()
        while (iterator.hasNext()) {

            val element = iterator.next()
            println("test04_IteratorOperator() in while loop element = $element")

            val isValueInRange = element.value in intRange
            assertTrue(isValueInRange)
        }

        // Check if exception is thrown
        val executable = Executable {
            iterator.next()
        }
        assertThrows(NoSuchElementException::class.java, executable, "No next element")

        println("test04_IteratorOperator() *********************")

        for (mynumb in range1) {
            println("test04_IteratorOperator() in for loop elem = $mynumb")

            val isValueInRange = mynumb.value in intRange
            assertTrue(isValueInRange)
        }

        println("test04_IteratorOperator() finished")
    }

}