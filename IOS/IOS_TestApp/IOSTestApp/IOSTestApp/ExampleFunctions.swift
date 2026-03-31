//
//  Swift functions
//
//

import Foundation
import os

// The return type is a tuple type - (Int, String)
func test() -> (value: Int, mes: String) {
    return (10, "hello")
}

// With no an argument label
func test(_ mes: String) -> Void {
    
    Logger().debug("test() \(mes)")
    
}

// With an argument label
func test(for mes: String) -> Void {
    
    Logger().debug("test() \(mes)")
    
}
