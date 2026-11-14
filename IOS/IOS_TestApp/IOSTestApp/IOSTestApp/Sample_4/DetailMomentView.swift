import SwiftUI

struct DetailMomentView: View {
    
    var moment: Moments
    
    var body: some View {
        Text("Hello world!")
    }
    
}

#Preview {
    DetailMomentView(moment: .shortSampleData)
        .sampleDataContainer()
}
