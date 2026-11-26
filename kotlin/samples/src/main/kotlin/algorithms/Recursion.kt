package algorithms

/**
 * Sample recursion
 */

/**
 * The nums of triangle:
 * n! = n + (n - 1) + (n - 2) + ... + 1
 */
fun triangle(n: Int): Int {
    if (n == 1) return 1
    else return n + triangle(n - 1)
}

/**
 * Factorials:
 * n! = n * (n - 1) * (n - 2) * ... * 1
 */
fun factorial(n: Int): Int {
    if (n == 0) return 1
    else return n * factorial(n - 1)
}