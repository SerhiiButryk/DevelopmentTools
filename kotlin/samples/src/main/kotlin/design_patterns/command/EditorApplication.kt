package design_patterns.command

import design_patterns.command.impl.CommandHistory
import design_patterns.command.impl.MoveCommand
import design_patterns.command.impl.RotateCommand

/**
 * Command design pattern
 *
 * Description:
 * This pattern allows to decouple a class which invoke some operation form a class which handle this operation.
 *
 * This allows the next features:
 * 1) Provide additional arguments to operation
 * 2) Redo / Undo operation
 * 3) Delay or schedule operation
 * 4) Process operation as a message in a queue
 * 5) Pass operation as a function argument
 *
 * Definition: Turn a request into a stand-alone object that contains all information about the request
 */

fun main() {

    // 1. User chose a command
    val command1 = MoveCommand(10, 10)
    val command2 = MoveCommand(20, 20)
    val command3 = RotateCommand(50)

    // 2. User applies commands
    val commandHistory = CommandHistory()
    commandHistory.addCommandAndExecute(command1)
    commandHistory.addCommandAndExecute(command2)
    commandHistory.addCommandAndExecute(command3)

    // 3. User decides to undo all commands
    commandHistory.undoLastCommand()
    commandHistory.undoLastCommand()
    commandHistory.undoLastCommand()

}