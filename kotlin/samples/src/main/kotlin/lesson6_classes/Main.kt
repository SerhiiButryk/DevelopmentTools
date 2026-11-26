package lesson6_classes

import lesson6_classes.cars.AudiCar
import lesson6_classes.transport.BusCar
import lesson6_classes.utils.Service

/**
 * Classes
 *
 * 1. All declarations are 'final' and 'public' by default.
 *
 * By setting 'final' modifier, Kotlin tries to resolve 'fragile class' issue, which is a problem in Java.
 * See - https://en.wikipedia.org/wiki/Fragile_base_class
 *
 * 2. Nested classes don't hold a reference to the outer class.
 *
 * 3. Interfaces can have concrete and abstract methods, but can't have states.
 *
 * However, they can have properties, but they should be abstract or have an  accessor implementations.
 * You must explicitly mark methods that you want to override as 'override'.
 * This ensures that you don't override method of base class by mistake.
 *
 * 4. Casting works only if classes or properties are not changed in subclasses.
 *
 * 5. Visibilities modifiers:
 *
 * Public, Protected, Private and Internal
 *
 * 6. Kotlin doesn't have namespaces
 *
 * 7. Sealed class - classes with a restricted hierarchy.
 *
 * Sealed classes don't have children outside the class declaration.
 *
 * Example:
 *
 * sealed class Expr {
 *     class Num(val value: Int) : Expr()
 *     class Sum(val left: Expr, val right: Expr) : Expr()
 * }
 */
fun main() {
    // 1. Interfaces
    val audiCar = AudiCar()
    audiCar.driveCar()

    // 2. Inner and nested classes
    val service = Service()
    service.ServiceHandler().handle()

    val serviceHelper = Service.ServiceHelper()

    // 3. Inheritance
    val bus = BusCar()
}