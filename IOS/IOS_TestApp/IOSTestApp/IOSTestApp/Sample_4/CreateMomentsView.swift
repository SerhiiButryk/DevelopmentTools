import SwiftUI
import PhotosUI
import SwiftData

struct CreateMomentsView: View {
    
    @State private var title = ""
    @State private var notes = ""
    @State private var selectedImage: PhotosPickerItem?
    @State private var image: Data?
    @State private var isShowingCancelComformation: Bool = false
    
    // Provided by SwiftUI
    @Environment(\.dismiss) private var dismiss
    // Accesses the env variables (Provided by us)
    @Environment(DataContainer.self) private var dataContainer
    
    var body: some View {
        NavigationStack {
            ScrollView {
                contentStack
            }
            // Dissmiss the keyboard when User scrolls the view
            .scrollDismissesKeyboard(.interactively)
            .navigationTitle("Create a moment!")
            // Fill all available width
            .frame(maxWidth: .infinity)
            .padding()
            .toolbar {
                
                ToolbarItem(placement: .confirmationAction) {
                    Button("Add", systemImage: "checkmark") {
                        insertMoment()
                    }
                    .disabled(title.isEmpty)
                }
                
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel", systemImage: "xmark") {
                        if title.isEmpty, notes.isEmpty, image == nil {
                            dismiss()
                        } else {
                            isShowingCancelComformation = true
                        }
                    }
                    .confirmationDialog("Discard Moments", isPresented: $isShowingCancelComformation) {
                        Button("Discard Moments", role: .destructive) {
                            dismiss()
                        }
                    }
                }
                
            }
        }
    }
    
    private var contentStack: some View {
        VStack(alignment: .leading) {
            
            TextField("Label text", text: $title)
                .font(.title.bold())
                .padding(.top, 40)
            
            Divider()
            
            TextField("Note text", text: $notes, axis: .vertical)
                .multilineTextAlignment(.leading)
                // Set 5 min line and max to Int.max
                .lineLimit(5...Int.max)
            
            photoPicker
            
        }
        // Fill all available width
        .frame(maxWidth: .infinity)
    }
    
    private var photoPicker: some View {
        PhotosPicker(selection: $selectedImage) {
            Group {
                if let image, let uiImage = UIImage(data: image) {
                    Image(uiImage: uiImage)
                        .resizable()
                        .scaledToFit()
                } else {
                    Image(systemName: "photo.badge.plus.fill")
                        .font(.largeTitle)
                        .frame(height: 250)
                        .frame(maxWidth: .infinity)
                        .background(Color(white: 0.4, opacity: 0.32))
                }
            }
            .clipShape(RoundedRectangle(cornerRadius: 16))
        }
        .onChange(of: selectedImage) {
            guard let selectedImage else { return }
            Task {
                image = try await selectedImage.loadTransferable(type: Data.self)
            }
        }
    }
    
    private func insertMoment() {
        let moment = Moments(
            tile: title,
            note: notes,
            imageData: image,
            timestamp: .now
        )
        dataContainer.context.insert(moment)
        do {
            try dataContainer.context.save()
            // Dismiss the current View
            dismiss()
        } catch {
            //Don't dismiss
        }
    }
    
}

#Preview {
    CreateMomentsView()
        .sampleDataContainer()
}
