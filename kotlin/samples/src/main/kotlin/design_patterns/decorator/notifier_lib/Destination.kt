package design_patterns.decorator.notifier_lib

class Destination(private val name: String) {
    override fun toString(): String {
        return name
    }
}