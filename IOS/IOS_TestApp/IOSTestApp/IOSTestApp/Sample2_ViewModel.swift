import SwiftUI

/*
    A simple object model class
 */
struct Plant: Identifiable {
    let id = UUID()
    let name: String
    let text: String = "Some text"
    let icon: String = "person"
}

@Observable
class Sample2_ViewModel {
    
    /*
        Changes to this field are automatically tracked by SwiftUI
        thanks to '@Observable'
     */
    var plantsList = [ Plant(name: "Apple"), Plant(name: "Lemon") ]
    
    func doAction() {
        plantsList.append(Plant(name: "Tomato"))
    }
    
}
