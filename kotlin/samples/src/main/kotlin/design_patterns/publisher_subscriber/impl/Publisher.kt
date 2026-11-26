package design_patterns.publisher_subscriber.impl

/**
 * Publisher interface allows you to add new type of Publisher classes and to make
 * them compatible with Subscriber classes.
 *
 * Adding new type of Publisher class doesn't require changes in existing Publisher / Subscriber classes.
 *
 * Additionally some optimizations can be made:
 * 1) Adding information about a type of changes
 * 2) Notifying only part of subscribers depending on some conditions
 * 3) Add a separate Controller class, if notify logic is complex, to send notifications to all parties
 */
interface Publisher {

    // true - subscriber is added
    // false - subscriber is not added as it was already added
    fun subscribe(subscriber: Subscriber): Boolean

    // true - subscriber is removed
    // false - subscriber is not removed as it was not added
    fun unsubscribe(subscriber: Subscriber): Boolean

    // List of Publisher events
    companion object {
        public const val EMAIL_SENT_EVENT = "EmailEditor.email_sent"
    }
}