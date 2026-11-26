package lesson6_classes.cars

/**
 * An interface declaration
 */
interface Engine {
    fun driveCar() = println("Drive Car [Engine]")
    fun stopCar() = println("Car is stopped")
}