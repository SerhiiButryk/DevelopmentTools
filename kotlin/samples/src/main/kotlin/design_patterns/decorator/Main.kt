package design_patterns.decorator

import design_patterns.decorator.notifier_lib.Message
import design_patterns.decorator.notifier_lib.Notifier
import design_patterns.decorator.notifier_lib.decorators.EmailNotifier
import design_patterns.decorator.notifier_lib.decorators.FacebookNotifier

/**
 * Decorator pattern
 */
fun main() {

    val notifier = EmailNotifier(FacebookNotifier(Notifier()))
    println(notifier)

    val message = Message()
    if (notifier != null) {
        notifier.send(message)
    }
}