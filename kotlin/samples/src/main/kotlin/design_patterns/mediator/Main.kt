package design_patterns.mediator

/**
 * Mediator pattern
 *
 * Description:
 * If you have a lot of classes that depend on each other, it's hard to reuse them.
 *
 * It's better to create a controller (mediator) class and allow them to talk to it.
 * Classes should talk to the mediator class instead of communicating directly with each other.
 *
 * In other words, they shouldn't know about other classes, only about the mediator.
 * Meanwhile, the mediator knows about every class and controls communication between them.
 *
 * This approach provides the following benefits:
 * 1. You can reuse code
 * 2. You can add new mediator classes
 * 3. You can reduce dependencies and prepare code for modularization.
 *
 * Definition: Reduce chaotic dependencies between objects.
 */
fun main() {

}