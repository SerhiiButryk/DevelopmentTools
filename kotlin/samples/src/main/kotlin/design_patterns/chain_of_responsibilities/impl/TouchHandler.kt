package design_patterns.chain_of_responsibilities.impl

/*
    Event handler interface
 */
interface TouchHandler {
    fun handleTouch(event: TouchEvent): Boolean {
        return false // Touch event is not handled
    }
}