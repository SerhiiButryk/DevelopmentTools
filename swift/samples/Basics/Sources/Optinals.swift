@main
struct OptionalsSample {
    
    static func main() {
        
        var person: Int? = nil

        if let value = person {
            print("Person is \(value)")
        } else {
            print("Person is nil")
        }
        
    }
}