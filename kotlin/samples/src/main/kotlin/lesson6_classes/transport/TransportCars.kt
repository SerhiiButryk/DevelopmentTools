package lesson6_classes.transport

import lesson6_classes.cars.Engine

/**
 * An abstract class
 */
abstract class TransportCars : Engine {

    // This is final and public
    fun getCarType() = "Transport Car"

    // To forbid overriding in subclasses it needs final keyword
    final override fun driveCar() {
        super.driveCar()
        println("Drive car [TransportCars]")
    }

    // This method can be overridden in base classes
    open fun checkEngine() {
        println("Drive car [TransportCars]")
    }

    // Abstract methods are open
    abstract fun cleanUp()
}