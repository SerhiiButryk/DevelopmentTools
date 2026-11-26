package design_patterns.publisher_subscriber

import design_patterns.publisher_subscriber.impl.EmailEditor
import design_patterns.publisher_subscriber.impl.NotificationManager

/**
 *  Publisher-Subscriber pattern.
 *
 *  Email creation application.
 *
 *  Description:
 *
 *  It demonstrates the use of Observer Pattern when all interested parties are notified about event.
 *
 *  Definition: Define a one-to-many dependency between objects so that when one object
 *  changes state, all its dependents are notified and updated automatically.
 */
fun main() {

    // 1. Create samples.main application class
    val emailEditor = EmailEditor()

    // 2. Subscribe to events in EmailEditor class
    val notificationManager = NotificationManager()
    emailEditor.subscribe(notificationManager)

    // 3. User creates email
    emailEditor.createEmail("Hello this is my first email")
    // 4. User sends email
    emailEditor.sentEmail()

    // 5. Email is sent so unsubscribe observers
    emailEditor.unsubscribe(notificationManager)

}