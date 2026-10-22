/**
*  Classes, structures and enumerations    
*/

struct Resolution {
    var width: Double = 0.0
    var height: Double = 0.0
}

enum AuthState {
    
    case initial
    case completed
    case inprogress

    mutating func next() {
        switch self {
        case .initial:
            self = .inprogress
        case .inprogress:
            self = .completed
        case .completed:
            self = .initial
        }
    }

}

@main
struct ClassesSample {
    
    static func main() {

        class Person {
            
            var name: String = ""
            
            var age: Int = 0 {
                
                willSet {
                    print("Will set age to \(age)")
                }
                
                didSet {
                    print("Did set age to \(age)")
                }

            }
        }

        let person = Person()
        person.age = 30

        // let id = ObjectIdentifier(person)
        // print("ObjectIdentifier for person: \(id)")

    }

}