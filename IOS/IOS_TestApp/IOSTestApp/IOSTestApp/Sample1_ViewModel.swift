import SwiftUI

@Observable
class Sample1_ViewModel {
    
    /*
        Changes to these field can be automatically tracked by SwiftUI
        thanks to '@Observable'
     */
    let activitiyList = ["Archery", "Baseball", "Basketball"]
    let colorList: [Color] = [.blue, .cyan, .green]
    
}
