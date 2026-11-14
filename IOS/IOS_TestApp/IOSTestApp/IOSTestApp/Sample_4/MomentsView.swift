import SwiftUI
import SwiftData

struct MomentsView: View {
    
    @Query(sort: \Moments.timestamp)
    private var moments: [Moments]
    
    @State private var showCreateMoments = false
    
    var body: some View {
        NavigationStack {
            ScrollView {
                momentsList
                    .frame(maxWidth: .infinity)
            }
            .navigationTitle("Gratefull Moments")
            .toolbar {
                ToolbarItem(placement: .primaryAction) {
                    Button {
                        showCreateMoments = true
                    } label: {
                        Image(systemName: "plus")
                    }
                    .sheet(isPresented: $showCreateMoments) {
                        CreateMomentsView()
                    }
                }
            }
            .overlay {
                if moments.isEmpty {
                    ContentUnavailableView {
                        Label("No moments yet", systemImage: "exclamationmark.circle.fill")
                    } description: {
                        Text("Create a new moment by tapping '+' button in the top")
                    }
                }
            }
        }
        
    }
    
    private var momentsList: some View {
        ForEach(moments) { moment in
            Text(moment.title)
        }
    }
    
}

#Preview("Moments") {
    MomentsView()
        .sampleDataContainer()
}

#Preview("No moments") {
    MomentsView()
        // Ask SwiftUI to create a data container for the selected types
        .modelContainer(for: [Moments.self])
}
