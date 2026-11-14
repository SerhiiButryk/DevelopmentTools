import SwiftUI

struct ActivitiesView: View {
    
    // This has 'Observable' attribute but 'State' attribute is required!
    @State private var model = Sample1_ViewModel()
    
    /*
        Changes to these fields are automatically tracked by SwiftUI
        and the dependent Views get updated accordingly
     */
    @State private var currActivity = ""
    @State private var currColor = Color.white
    
    init() {
        currActivity = model.currActivity
        currColor = model.currColor
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
                
                model.moveNext()
                
                withAnimation {
                    currColor = model.getCurrColor()
                    currActivity = model.getCurrActivity()
                }
                
            }.padding()
            .buttonStyle(.borderedProminent)
            
        }
    }
    
}

#Preview("Portrait Prev", traits: .portrait) {
    ActivitiesView()
}

#Preview("Landscape Prev", traits: .landscapeLeft) {
    ActivitiesView()
}
