package lesson6_classes.utils

class Service {

    fun performService(serviceName: String) {
        println("performService, $serviceName")
    }

    // This class is equivalent to a static class in Java
    class ServiceHelper {
        fun lookUpService(service: String) {}
    }

    // This class is equivalent to a nested class which holds a reference
    // to the outer class in Java
    inner class ServiceHandler {
        fun handle() {

            // Use implicit reference
            performService("")

            // Or use explicit reference
            this@Service.performService("")
        }
    }

}