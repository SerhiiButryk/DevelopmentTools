package design_patterns.command.impl

class CommandHistory {

    private val commandsList = mutableListOf<ElementCommand>()

    fun addCommandAndExecute(command: ElementCommand) {
        if (!commandsList.contains(command)) {
            commandsList.add(command)
            println("CommandHistory.addCommand command $command is added")
        }

        command.applyAction()
    }

    fun undoLastCommand() {
        if (commandsList.lastIndex == -1)
            return

        val elementAction = commandsList.get(commandsList.lastIndex)

        // Do operations to undo command

        // Done... So remove command from history
        commandsList.remove(elementAction)

        println("CommandHistory.undoLastCommand command $elementAction is undone")
    }

}