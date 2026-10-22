/**
*  Collections    
*/

@main
struct CollectionsSample {
    
    static func main() { 
        
        var names: [String] = ["Apple", "Lemon", "Bannana"]

        names.sort(by: { (s1: String, s2: String) -> Bool in
            return s1 < s2
        })

        // OR

        names.sort(by: { s1, s2 -> Bool in
            return s1 < s2
        })

        // OR

        names.sort(by: { s1, s2 -> Bool in
            s1 < s2
        })

        // OR 

        names.sort() { s1, s2 -> Bool in
            s1 < s2
        }

        // OR if the function has only one closure argument, you can omit the parentheses

        names.sort { s1, s2 -> Bool in
            s1 < s2
        }

        // OR using some provided operators

        names.sort(by: >)

        print(names)
    } 
    
}