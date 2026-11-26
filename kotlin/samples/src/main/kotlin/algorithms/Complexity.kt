package algorithms

/**
 *
 * Asymptotic analysis:
 *
 * 1) Time - a relative time which is spent by an algorithm
 *
 *  - Worst case
 *  - Best case
 *  - Average case
 *
 * 2) Memory - a relative memory which is used by an algorithm
 *
 * Common complexity patterns:
 * O(1)
 * O(n), O(n*n), O(n+m) ...
 * O(log(n))
 * O(sqrt(n))
 */

fun main() {
    f7(10)
}

fun f1() { // Complexity - O(1)

    // Fixed number of operation

    val c = 4
    for (i in 0 .. c) {
        println(c)
    }
}

fun f2(n: Int) { // Complexity - O(n)

    // Number of operation depends on external non-constant value

    val c = n
    for (i in 0 .. c) {
        println(c)
    }
}

fun f3(n: Int, m: Int) { // Complexity - O(max(n, m)) or O(n + m)

    for (i in 0 .. n) {
        println(n)
    }

    for (i in 0 .. m) {
        println(m)
    }
}

fun f4(n: Int, m: Int) { // Complexity - O(n * m)

    for (i in 0 .. n) {
        for (j in 0 .. m) {
            println("$i $j")
        }
    }
}

fun f5(n: Int) { // Complexity - O(n * n) or O(n^2)

    for (i in 0 .. n) {
        for (j in 0 .. n) {
            println("$i $j")
        }
    }
}

fun f6(n: Int, m: Int) { // Complexity - O(n^2*m)

    for (i in 0 .. n) {
        for (j in 0 .. n) {
            for (k in 0 .. m) {
                println("$i $j $k")
            }
        }
    }
}

fun f7(n: Int) { // Complexity - O(n*n) or O(n^2)

    for (i in 0 until n) {
        for (j in i until  n) { // n + (n - 1) + (n - 2) + ... + 1 = (n*(n+1)) / 2 ~ n*n
            println("$i $j")
        }
    }
}