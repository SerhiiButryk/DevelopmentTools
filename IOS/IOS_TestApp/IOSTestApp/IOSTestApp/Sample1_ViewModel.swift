import SwiftUI

// The 'Observable' class properties are watched by SwiftUI
// so when they've been changed, view would have change accordingly
@Observable
class Sample1_ViewModel {
    
    let activitiyList = ["Archery", "Baseball", "Basketball"]
    let colorList: [Color] = [.blue, .cyan, .green]
    
    var currActivity: String = ""
    var currColor: Color = Color.white
    
    var index = 0
    
    init() {
        currActivity = activitiyList[index]
        currColor = colorList[index]
    }
    
    func moveNext() {
        index += 1
        if index >= colorList.count {
            index = 0
        }
    }
    
    func getCurrActivity() -> String {
        return activitiyList[index]
    }
    
    func getCurrColor() -> Color {
        return colorList[index]
    }
    
}
