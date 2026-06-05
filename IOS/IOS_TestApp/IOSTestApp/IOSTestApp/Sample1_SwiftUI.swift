import SwiftUI

/*
    A view implemented in SwiftUI
 */
struct ActivitiesView: View {
    
    // SwiftUI automatically updates UI when these values are changed
    @State private var model = Sample1_ViewModel()
    
    @State private var currActivity: String = ""
    @State private var currColor: Color = Color.white
    
    @State private var index = 0
    
    init() {
        currActivity = model.activitiyList[0]
        currColor = model.colorList[0]
    }
    
    var body: some View {
        
        VStack {
            
            Text("Hello !")
                .font(.title.bold())
            
            Circle()
                .fill(currColor)
                .padding()
                .overlay(
                    Image(systemName: "figure.\(currActivity.lowercased())")
                        .foregroundColor(.white)
                        .font(.system(size: 140))
                )
            
            Text("This is \(currActivity)")
                .font(.body.bold())
            
            Button("Try again") {
                
                index += 1
                if index >= model.colorList.count {
                    index = 0
                }
                
                withAnimation {
                    currColor = model.colorList[index]
                    currActivity = model.activitiyList[index]
                }
                
            }.padding()
            .buttonStyle(.borderedProminent)
            
        }
    }
    
}

struct ActivitiesViewPreview : PreviewProvider {
    static var previews: some View {
        ActivitiesView()
    }
}
