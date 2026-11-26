package samples.custom_number_class

/**
 * Custom class which is a wrapper for Int values.
 */
class MyIntNumber(val value: Int) : Comparable<MyIntNumber> {

    /**
     * compareTo() compares MyNumber objects by its value
     */
    override fun compareTo(other: MyIntNumber): Int {
        return this.value.compareTo(other.value)
    }

    /**
     * rangeTo() creates a range from MyNumber objects
     */
    operator fun rangeTo(number: MyIntNumber): ClosedRange<MyIntNumber> {
        println("rangeTo(), [$value:${number.value}]")

        val rangeStart = this
        val rangeEnd = number

        return object : ClosedRange<MyIntNumber> {
            override val endInclusive: MyIntNumber get() = rangeEnd
            override val start: MyIntNumber get() =  rangeStart
        }
    }

    /**
     * Function for adding 2 MyNumber objects
     */
    operator fun plus(other: MyIntNumber): MyIntNumber {
        return MyIntNumber(this.value + other.value)
    }

    override fun equals(other: Any?): Boolean {
        // If references are equal then objects are equal
        if (other === this) return true
        // If types are different then objects are not equal
        if (other !is MyIntNumber) return false
        // Compare values
        return other.value == this.value
    }

    override fun hashCode(): Int {
        return value.hashCode() * 31
    }

    // Operator 'in' support for int range
    operator fun contains(range: IntRange): Boolean {
        return range.contains(this.value)
    }

    // Operator 'in' support for int
    operator fun contains(range: Int): Boolean {
        return range == value
    }

    override fun toString(): String {
        return "$value"
    }
}

/**
 * Extension function for iteration over a range of MyNumber objects
 */
operator fun ClosedRange<MyIntNumber>.iterator(): Iterator<MyIntNumber> {
    return object : Iterator<MyIntNumber> {
        // At the beginning 'current' is a first element in a range
        private var current = start

        override fun hasNext(): Boolean {
            return current <= endInclusive
        }

        override fun next(): MyIntNumber {
            if (!hasNext()) {
                throw NoSuchElementException("No next element")
            }
            return current.apply {
                current += MyIntNumber(1)
            }
        }
    }
}