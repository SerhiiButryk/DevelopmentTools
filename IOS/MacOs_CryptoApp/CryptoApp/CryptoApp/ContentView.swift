import SwiftUI
import CryptoFramework

struct ContentView: View {
    
    let crypto = CryptoKit()
       
       var body: some View {
           VStack {
               
               let currentDirectoryURL = URL.currentDirectory()
               let workingPath = currentDirectoryURL.path
               
               Text("Crypto sample")
                   .font(.title).padding(.bottom, 40)
               
               Text("Working dir: " + workingPath)
                   .textSelection(.enabled)
               
               VStack {
                   
                   Button("Encrypt") {
                       crypto.encrypt()
                   }
                   
                   Button("Decrypt") {
                       crypto.decrypt()
                   }
               }
               
           }
           .padding()
       }
}

#Preview {
    ContentView()
}
