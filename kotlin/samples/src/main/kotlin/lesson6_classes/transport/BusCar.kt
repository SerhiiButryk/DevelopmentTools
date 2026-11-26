package lesson6_classes.transport

/**
 * A derive class
 */
class BusCar : TransportCars() {

    // Overrides method from TransportCars class
    override fun checkEngine() {
        super.checkEngine()
        println("Drive car [BusCar]")
    }

    // Overrides method from Engine interface
    override fun stopCar() {
        super.stopCar()
        println("Drive car [BusCar]")
    }

    // Overrides abstract method
    override fun cleanUp() {
        println("Clean Up")
    }

}