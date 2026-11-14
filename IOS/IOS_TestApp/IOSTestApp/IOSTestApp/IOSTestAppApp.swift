import SwiftUI

@main
struct IOSTestAppApp: App {
    
    let dataContainer = DataContainer()
    @State private var isPresented = false
    
    var body: some Scene {
        WindowGroup {
            VStack {
                Button("Open Gratefull moments") {
                    isPresented = true
                }
            }.sheet(isPresented: $isPresented) {
                CreateMomentsView()
                    // Exposes an object to the whole View hierarchy
                    .environment(dataContainer)
            }
        }
        .modelContainer(dataContainer.modelContainer)
    }
}
