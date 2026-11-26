package algorithms

/**
 * Sample sorted array
 */

class SortedArray(size: Int) {
    private val array: Array<Int?> = Array(size) { null }
    private var nElements = 0

    // Complexity: O(N)
    fun insert(element: Int) {

        if (array.size == nElements) return

        if (nElements == 0) {
            array[nElements++] = element
            return
        }

        val last = nElements - 1
        var insertIndex = nElements

        for (i in 0..last) {
            if (element < array[i]!!) {
                insertIndex = i
                break
            }
        }

        for (i in last downTo insertIndex) {
            array[i + 1] = array[i]
        }

        array[insertIndex] = element
        nElements++
    }

    // Complexity: O(N)
    fun remove(element: Int) {
        var k = -1

        val last = nElements - 1
        for (i in 0..last) {
            if (array[i] == element) {
                k = i
                break
            }
        }

        if (k == -1) return

        // |1|2|3|4| --> |1|3|4|4| nElem = 4
        //    ^
        for (j in k until last) {
            array[j] = array[j+1]
        }

        // |1|3|4|4| -> |1|3|4|null| nElem = 4
        array[nElements - 1] = null

        // |1|3|4|null| nElem = 3
        nElements--
    }

    // Binary search (array is sorted)
    // Complexity: O(log(N))
    fun find(element: Int): Int {

        if (nElements == 0) return -1

        var begin = 0
        var end = nElements - 1

        while (true) {

            val middle = (end + begin) / 2

            if (array[middle] == element) {
                return middle
            } else if (begin > end) {
                return -1
            } else {
                if (array[middle]!! > element) {
                    end = middle - 1
                } else {
                    begin = middle + 1
                }
            }

        }

    }

    fun log() {
        print("sz = ${array.size} -> [")
        for (i in array) {
            print("$i,")
        }
        println("]")
    }
}