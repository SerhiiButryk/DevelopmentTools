package design_patterns.command.impl

/**
 * Concrete command. Represent move operation on a graphic element.
 */
class MoveCommand(private val x: Int, private val y: Int) : ElementCommand {

    override fun applyAction() {
        println("applyAction, move to $x $y coordinates")
    }

}