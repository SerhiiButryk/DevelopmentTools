package design_patterns.decorator.notifier_lib.decorators

import design_patterns.decorator.notifier_lib.Destination
import design_patterns.decorator.notifier_lib.Message
import design_patterns.decorator.notifier_lib.Notify

class FacebookNotifier(private val notifier: Notify) : Notify by notifier {
    override fun send(message: Message) {
        val destination = Destination("Facebook")
        println("Sending message to $destination ...")

        notifier.send(message)
    }

    override fun toString(): String {
        return "Facebook-$notifier"
    }
}