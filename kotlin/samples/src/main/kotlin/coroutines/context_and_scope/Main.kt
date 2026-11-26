package coroutines.context_and_scope

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * Coroutine scope and context
 *
 * Coroutine scope has only one property which is CoroutineContext.
 * In other words a scope provides access to CoroutineContext in any coroutine or suspend function.
 * We can modify or get values from it.
 */
fun main() {

    val context = CoroutineName("MyCoroutineName1")

    // Every coroutine context element is a context itself
    // So we can use it as a context
    val element : CoroutineContext = context

    val job = Job()

    // We can combine 2 or more context elements
    // The result is CombineCoroutine object
    val newContext = context + job

    // We can use fold operation to go through elements in the context
    // Here for instance we put every element in a list and then print it
    printContext(newContext)

    // We can remove an element for the context
    val newContext2 = newContext.minusKey(CoroutineName)
    printContext(newContext2)
}

fun printContext(context: CoroutineContext) {
    val list = mutableListOf<CoroutineContext>()
    val list2 = context.fold(list) { list, element ->
        list.add(element)
        list
    }
    list2.forEach { element -> println(element) }
}