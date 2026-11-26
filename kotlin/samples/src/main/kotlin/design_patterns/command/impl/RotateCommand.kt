package design_patterns.command.impl

/**
 * Concrete command. Represent rotate operation on a graphic element.
 */
class RotateCommand(private val angle: Int) : ElementCommand {

    override fun applyAction() {
        println("applyAction, rotating on $angle degrees")
    }

}