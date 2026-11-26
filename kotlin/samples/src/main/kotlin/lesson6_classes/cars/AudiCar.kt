package lesson6_classes.cars

/**
 * Calling specific base class implementation
 */
class AudiCar : Engine, MyCar {

    override fun driveCar() {
        super<Engine>.driveCar()
        super<MyCar>.driveCar()
        println("driveCar [AudiCar]")
    }

}