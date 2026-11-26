package algorithms

/**
 * Sample Ring Buffer
 */

class RingBuffer(size: Int) {

    private val array = Array(size) { 0 }
    private var front = 0
    private var rear = -1
    private var nItems = 0

    fun insert(value: Int) {
        if (rear == array.lastIndex) {
            rear = -1
        }
        array[++rear] = value
        nItems++
    }

    fun peek() = array[rear]

    fun pickFront(): Int {
        val elem = array[front++]
        if (front == array.size) {
            front = 0
        }
        nItems--
        return elem
    }

    fun isEmpty() = nItems == 0
    fun isFull() = nItems == array.size

}