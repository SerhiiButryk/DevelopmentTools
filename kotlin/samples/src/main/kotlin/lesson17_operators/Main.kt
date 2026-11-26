package lesson17_operators

import java.lang.IndexOutOfBoundsException

/**
 * Operators
 * -------------------------
 *
 * Binary operators:
 * -------------------------
 * |  a * b   |  times  |
 * |  a / b   |  div    |
 * |  a % b   |  mod    |
 * |  a + b   |  plus   |
 * |  a - b   |  minus  |
 * --------------------------
 * |  *=   |  timesAssign  |
 * |  /=   |  divAssign    |
 * |  %=   |  modAssign    |
 * |  +=   |  plusAssign   |
 * |  -=   |  minusAssign  |
 * ---------------------------
 *
 * Bitwise operators
 *  | shl   | m << n
 *  | shr   | m >> n
 *  | ushr  | m >> n
 *  | and   | n && n
 *  | or    | n || n
 *  | xor   | m ^ n
 *  | inv   | ~n
 *  -----------------------------
 *
 *  Unary operators
 *  | +n         |  unaryPlus   |
 *  | -n         |  unaryMinus  |
 *  | !n         |  not         |
 *  | ++n, n++   |  inc         |
 *  | --n, n--   |  dec         |
 *  ------------------------------
 *
 *  Other operators
 *  ------------------------------
 *  == , !=         -> equals()
 *  ===             -> reference comparison
 *  >, <, >=, <=    -> compareTo()
 *  in              -> contains()
 *  [index]         -> get(), set()
 *  10..20          -> rangeTo()
 *  element in list -> iterator()
 *
 *  -------------------------------
 *  Notes:
 *
 *  1) ++n and ++n with user defined types works the same as with primitive types.
 *  Compiler invokes postfix or prefix functions depending on instruction order.
 *
 *  2) You should never implement plus() and plusAssign() operators together. It creates ambiguity.
 *  You should choose ether one or other operator. This also relates to other arithmetic operators.
 *
 *  3) Usually contains() and rangeTo() functions are extension methods.
 *  ---------------------------------
 */

class Number(var number: Int) {
    override fun toString(): String {
        return number.toString()
    }
}

/**
 * '+' operator
 */
operator fun Number.plus(other: Number): Number {
    return Number(this.number + other.number)
}

/**
 * '*' operator
 *
 * Parameters and function return type can be different.
 * For example, the next code will return string and it accepts char
 */
operator fun Char.times(count: Int): String {
    return toString().repeat(count)
}

/**
 * Index operator
 */
class MutablePoint(var x: Int, var y: Int) {
    // call to get x and y coordinates
    operator fun get(index: Int): Int {
        return when(index) {
            0 -> x
            1 -> y
            else -> throw IndexOutOfBoundsException("Incorrect index")
        }
    }
    // call to set x and y coordinates
    operator fun set(index: Int, value: Int) {
        when(index) {
            0 -> x = value
            1 -> y = value
            else -> throw IndexOutOfBoundsException("Incorrect index")
        }
    }
}

/**
 * In operator
 */
operator fun Number.contains(value: Int): Boolean {
    return number == value
}

fun main() {

    println("------------------------------------ Operators ----------------------------------------")

    // plus operator
    val number1 = Number(1)
    val number2 = Number(1)
    var result = number1 + number2
    println(" $number1 + $number2 = $result")

    // [] operator
    val point = MutablePoint(10, 20)
    var x = point[0]
    var y = point[1]
    println("x = $x, y = $y")

    point[0] = 30
    point[1] = 40
    x = point[0]
    y = point[1]
    println("x = $x, y = $y")

    // in operator
    val numb2 = Number(10)
    val isTen = 10 in numb2
    println("isTen $isTen")

    println("------------------------------------ Ranges ----------------------------------------")

    // rangeTo()
    val range1 = 10..20
    println("${range1::class.simpleName} : ${range1.first} - ${range1.last} - ${range1.step}")
    val range2 = 10 until 20 step 3
    println("${range2::class.simpleName} : ${range2.first} - ${range2.last} - ${range2.step}")
    // Empty range
    val range3 = 10 downTo 20
    println("${range3::class.simpleName} : ${range3.first} - ${range3.last} - ${range3.step}")
    val range4 = 20 downTo 1
    println("${range4::class.simpleName} : ${range4.first} - ${range4.last} - ${range4.step}")
    // Reversed range
    val range5 = (10..20).reversed()
    println("${range5::class.simpleName} : ${range5.first} - ${range5.last} - ${range5.step}")
}