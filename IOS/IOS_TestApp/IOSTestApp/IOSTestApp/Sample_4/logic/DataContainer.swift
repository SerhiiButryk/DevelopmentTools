import SwiftUI
import SwiftData

@Observable // Declares that changes in this class observed by Swift UI
@MainActor // Declares that the code is accessed only by the Main UI
class DataContainer {
    
    // Creates a sample data container with a data sample
    static let dataSample = DataContainer(includeSampleData: true)
    
    // Declares a class to access Swift database implementation
    let modelContainer: ModelContainer
    
    var context: ModelContext {
        modelContainer.mainContext
    }
    
    init(includeSampleData: Bool = false) {
        // List data types we are going to manage
        let scheme = Schema([
            Moments.self
        ])
        
        let modelConfiguration = ModelConfiguration(schema: scheme, isStoredInMemoryOnly: includeSampleData)
        
        do {
            modelContainer = try ModelContainer(for: scheme, configurations: [modelConfiguration])
            if includeSampleData {
                loadSampleData()
            }
        } catch {
            fatalError("Could not create model, error = '\(error)'")
        }
    }
    
    private func loadSampleData() {
        for item in Moments.sampleData {
            context.insert(item)
        }
    }
    
}


// Makes data container available to all View hierarchy
extension View {
    func sampleDataContainer() -> some View {
        self
            .environment(DataContainer.dataSample)
            .modelContainer(DataContainer.dataSample.modelContainer)
    }
}


