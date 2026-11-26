package design_patterns.chain_of_responsibilities

import design_patterns.chain_of_responsibilities.impl.TouchEvent
import design_patterns.chain_of_responsibilities.impl.TouchHandler
import design_patterns.chain_of_responsibilities.impl.ui_components.UIContainer
import design_patterns.chain_of_responsibilities.impl.ui_components.UIPanel
import design_patterns.chain_of_responsibilities.impl.ui_components.Window

/**
 * Chain of responsibilities pattern
 *
 * Description:
 * Idea: Instead of managing all different behaviors in one class extract them in a separate class.
 * This pattern allows to create a chain of handlers. So, there can 2 models.
 *
 * Model 1:
 * If request is not processed by a handler, it is passed to another handler.
 *
 * Model 2:
 * Request is passed sequentially to next handled, if it is available.
 *
 * In this pattern, each of the handles can be reused.
 * Definition: Let you pass requests along a chain of handlers.
 * Example demonstrates the use of this pattern to propagate a touch UI event to all widgets.
 */

fun main() {

    // 1. Create application classes
    val window = Window()
    val container = UIContainer()
    val uipannel = UIPanel()

    // 2. Set object in chain
    container.setParent(window)
    uipannel.setParent(container)

    // The chain will be the next :
    // UI Panel -> UI Container -> Application Window

    // Set touch handler
    window.setTouchHandler(object : TouchHandler {
        override fun handleTouch(event: TouchEvent): Boolean {
            println("Touch event is handled by Window")
            return true
        }
    })

    // 3. User touches UI Panel
    uipannel.dispatchTouchEvent(TouchEvent())

}