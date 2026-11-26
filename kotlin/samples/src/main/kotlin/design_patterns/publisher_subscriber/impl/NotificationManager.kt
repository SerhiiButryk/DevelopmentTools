package design_patterns.publisher_subscriber.impl

/**
 * Show notification for User about event in this application
 */
class NotificationManager : Subscriber {

    override fun notifyAboutEvent(eventName: String, additionalData: Any?) {
        println("NotificationManager::notifyAboutEvent, got event: $eventName")

        if (eventName == Publisher.EMAIL_SENT_EVENT) {
            println("NotificationManager::notifyAboutEvent, notify User that email is sent")
            // Show notification to User
            showEmailSentNotification()
        }

        println("NotificationManager::notifyAboutEvent, finished")
    }

    private fun showEmailSentNotification() {
        println("NotificationManager::showEmailSentNotification, Email is sent")
    }

}