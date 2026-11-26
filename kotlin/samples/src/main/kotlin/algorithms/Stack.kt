package algorithms

import kotlin.text.iterator

/**
 * Sample Stack
 */

fun main() {
    println(makeParenthesisCheck("c[d]")) // True
    println(makeParenthesisCheck("a{b[c]d}e")) // True
    println(makeParenthesisCheck("a{b(c]d}e")) // False
    println(makeParenthesisCheck("a[b{c}d]e}")) // False
    println(makeParenthesisCheck("a{b(c)")) // False
}

fun makeParenthesisCheck(test: String): Boolean {

    val stack = MyStack<Char>(20)

    for (char in test) {

        if (char == '[' || char == '{' || char == '(' ) {

            stack.push(char)

        } else if (char == ']' || char == '}' || char == ')') {

            if (stack.isEmpty()) return false

            val lastChar = stack.pop()

            if (!parenthesesMatches(open = lastChar, close = char)) {
                return false
            }

        }

    }

    return stack.isEmpty()

}

fun parenthesesMatches(open: Char, close: Char): Boolean {
    return when (open) {
        '(' if close == ')' -> {
            true
        }
        '[' if close == ']' -> {
            true
        }
        '{' if close == '}' -> {
            true
        }
        else -> {
            false
        }
    }
}

class MyStack<T>(size: Int) {

    private val arr = Array<Any?>(size) { null }
    private var top = -1

    fun push(value: T) {
        arr[++top] = value
    }

    fun pop(): T {
        val value = arr[top]
        arr[top] = null
        --top
        return value as T
    }

    fun peek(): T {
        val value = arr[top]
        return value as T
    }

    fun isEmpty() = top == -1
    fun isFull() = top == arr.lastIndex
}
