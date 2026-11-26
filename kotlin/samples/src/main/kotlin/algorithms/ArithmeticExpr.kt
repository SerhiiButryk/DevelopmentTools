package algorithms

/**
 * Sample arithmetic parser algorithm
 */

// To simplify programming, we will assume
// that operands consist of only a single digit.
// Our program calculates and outputs the result of a postfix expression.
// Remember that operands contain no more than one digit.
fun main() {
    println(calculate("3*(4+5)-6/(1+2)"))
}

fun calculate(input: String): Int {

    val stack = MyStack<Int>(10)
    val postfixExpr = toPostfixExpr(input)

    postfixExpr.forEach { char ->
        when(char) {
            '+', '-', '/', '*' -> {
                val op2 = stack.pop()
                val op1 = stack.pop()
                val calc = when (char) {
                    '+' -> { op1 + op2 }
                    '-' -> { op1 - op2 }
                    '/' -> { op1 / op2 }
                    '*' -> { op1 * op2 }
                    else -> throw RuntimeException()
                }
                stack.push(calc)
            }
            else -> {
                stack.push(char.code - '0'.code)
            }
        }
    }

    return stack.pop()
}

// "A+B*C" -> "ABC*+"
// "(A+B)*C" -> "AB+C*"
// "A*(B+C)-D/(E+F)" -> "ABC+*DEF+/-"
fun toPostfixExpr(input: String): String {

    val opStack = MyStack<Char>(10)

    var output = ""

    input.forEach { char ->
        when (char) {
            '(' -> {
                opStack.push(char)
            }
            ')' -> {
                while(!opStack.isEmpty()) {
                    val top = opStack.pop()
                    if (top == '(') break
                    output += top
                }
            }
            '+', '-', '/', '*' -> {

                val getPriority: (Char) -> Int = { char ->
                    if (char == '+' || char == '-') 1 else 2
                }

                while(!opStack.isEmpty()) {
                    val top = opStack.pop()
                    if (top == '(') {
                        opStack.push(top)
                        break
                    }

                    val prior1 = getPriority(char)
                    val prior2 = getPriority(top)

                    if (prior1 > prior2) {
                        opStack.push(top)
                        break
                    } else {
                        output += top
                    }
                }

                opStack.push(char)
            }
            else -> {
                output += char
            }
        }
    }

    while(!opStack.isEmpty()) {
        val top = opStack.pop()
        output += top
    }

    return output
}