package design_patterns.publisher_subscriber.impl

/**
 * All classes who want to get notifications about some updates from Publisher classes
 * need to implement this interface. In this way, Publisher classes are independent from
 * Subscribers classes. They are communication through the same interface.
 */
interface Subscriber {
    // eventName - information about event type
    // additionalData - some additional data or context of changes
    fun notifyAboutEvent(eventName: String, additionalData: Any?)
}