package design_patterns.chain_of_responsibilities.impl.ui_components

import design_patterns.chain_of_responsibilities.impl.TouchEvent
import design_patterns.chain_of_responsibilities.impl.TouchHandler

open class View {

    private var parent: View? = null
    private var touchHandler: TouchHandler? = null

    /*
        Set action to perform in response to the event
     */
    fun setTouchHandler(touchHandler: TouchHandler) {
        this.touchHandler = touchHandler
    }

    /*
        Set next handler in chain
     */
    fun setParent(view: View) {
        parent = view
    }

    /*
        Decide whether to handle or proceed
     */
    fun dispatchTouchEvent(event: TouchEvent) {
        println("View.dispatchTouchEvent $this IN")

        var isHandled = false
        if (touchHandler != null) {
            isHandled = touchHandler!!.handleTouch(event)
        }

        if (!isHandled && parent != null) {
            println("View.dispatchTouchEvent touch event is not handled, pass event to parent")
            parent!!.dispatchTouchEvent(event)
        } else {
            println("View.dispatchTouchEvent touch event is handled by $this")
        }

        println("View.dispatchTouchEvent $this OUT")
    }

}