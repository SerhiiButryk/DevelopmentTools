/**
*  Functions    
*/

// The return type is a tuple type - (Int, String)
func test() -> (value: Int, mes: String) {
    return (10, "hello")
}

// With no argument labels
func test(_ mes: String) -> Void {
    print("test() \(mes)")
}

// With an argument label
func test(for mes: String) -> Void {
    print("test() \(mes)")
}

// Calling the functions
// test(for: "Hi John")