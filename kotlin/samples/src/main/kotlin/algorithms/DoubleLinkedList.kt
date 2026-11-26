package algorithms

/**
 * Sample Double Linked List
 */

class Node<T> {
    var prev: Node<T>? = null
    var next: Node<T>? = null
    var data: T? = null
}

class DoubleLinkedList<T> {

    private var first: Node<T>? = null
    private var last: Node<T>? = null

    fun insertFirst(value: T) {

        val node = Node<T>()
        node.data = value

        if (isEmpty()) {
            last = first
        } else {
            require(first != null)
            node.next = first
            first!!.prev = node
        }

        first = node

    }

    fun insertLast(value: T) {
        val node = Node<T>()
        node.data = value

        if (isEmpty()) {
            first = node
        } else {
            require(last != null)
            node.prev = last
            last!!.next = node
        }

        last = node
    }

    fun insertAfter(key: T, value: T) {

        if (isEmpty()) return

        var curr = first
        while (curr != null) {
            if (curr.data == key) break // Found !
            curr = curr.next
        }

        if (curr != null) {

            val node = Node<T>()
            node.data = value

            node.next = curr.next
            node.prev = curr

            if (last === curr) {
                // Insert after the end of the list
                last = node
            } else {
                curr.next!!.prev = node
            }

            curr.next = node
        }

    }

    fun deleteFirst() {

        if (isEmpty()) return

        if (first === last) {
            last = first?.next
        }

        first?.next?.prev = null

        first = first?.next
    }

    fun deleteLast() {

        if (isEmpty()) return

        if (first === last) {
            first = null
            last = null
            return
        }

        last?.prev?.next = null
        last = last?.prev
    }

    fun deleteBy(key: T) {
        if (isEmpty()) return

        var curr = first
        while (curr != null) {
            if (curr.data == key) break // Found !!!
            curr = curr.next
        }

        if (curr != null) {

            if (first === curr) {
                first = curr.next
            } else {
                curr.prev?.next = curr.next
            }

            if (last === curr) {
                last = curr.prev
            } else {
                curr.next?.prev = curr.prev
            }

            curr.next = null
            curr.prev = null

        }
    }

    fun isEmpty() = first == null

    fun displayForward() {
        println(">>> last = '$last' value = '${last?.data}'")
        println(">>> first = '$first' value = '${first?.data}'")
        var curr = first
        print("[")
        while (curr != null) {
            print(" ${curr.data} ")
            curr = curr.next
        }
        println("]")
    }

    fun displayBackward() {
        var curr = last
        print("[")
        while (curr != null) {
            print(" ${curr.data} ")
            curr = curr.prev
        }
        println("]")
    }

}