import SwiftUI
import os

struct ContentView: View {
    
    var body: some View {
        VStack {
            Image(systemName: "globe")
                .imageScale(.large)
                .foregroundStyle(.tint)
            
            let version = UIDevice.current.systemVersion
            
            Text("Hello, world! \n Hi \(version) IOS !")
        }
        .padding()
    }
    
}

#Preview {
    ContentView()
}
