package design_patterns.publisher_subscriber.impl

/**
 * Suppose we have an application for creation and sending emails.
 * This class manages email editing features. It notifies about occured events other parties.
 */
class EmailEditor : Publisher {

    // a lot of states and fields

    private val subscribersList = mutableListOf<Subscriber>()

    public fun createEmail(text: String) {
        println("EmailEditor::createEmail, called")

        // a lot of code here
    }

    public fun sentEmail() {
        println("EmailEditor::sentEmail, called")

        // a lot of code here

        // notify that email is sent
        notifyAllThatEmailIsSent()
    }

    override fun subscribe(subscriber: Subscriber): Boolean {
        println("EmailEditor::subscribe, called")

        if (!subscribersList.contains(subscriber)) {
            subscribersList.add(subscriber)
            println("EmailEditor::subscribe, added + $subscriber")
            return true
        }

        return false
    }

    override fun unsubscribe(subscriber: Subscriber): Boolean {
        println("EmailEditor::unsubscribe, called")
        val result = subscribersList.remove(subscriber)
        println("EmailEditor::unsubscribe, is subscriber removed: $result")
        return result
    }

    private fun notifyAllThatEmailIsSent() {
        println("EmailEditor::notifyAllThatEmailIsSent, called")
        for (subscriber in subscribersList) {
            subscriber.notifyAboutEvent(Publisher.EMAIL_SENT_EVENT, null)
        }
        println("EmailEditor::notifyAllThatEmailIsSent, finished")
    }

    // a lot of methods
}