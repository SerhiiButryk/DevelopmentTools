import SwiftUI

struct Sample3_TripUI: View {
    
    @State var model = TripViewModel()
    
    var body: some View {
        VStack(alignment: .leading) {
            
            Text("Title section")
                .font(.system(size: CGFloat(26)))
                .padding()
            
            ScrollView(.horizontal) {
                HStack {
                    
                    ForEach(0..<5) { _ in
                        TripCard(model: $model)
                    }
                    
                }
                .frame(height: 220)
            }
            
            Spacer()
            
            VStack(alignment: .leading) {
                
                Text("Title section")
                    .font(.system(size: CGFloat(26)))
                
                Text("Subtitle section")
                    
                
            }.padding()
            
            ScrollView(.horizontal) {
                HStack {
                    
                    ForEach(0..<5) { _ in
                        TripCard(model: $model)
                    }
                    
                }
            }
            
        }
    }
}

struct TripCard: View {
    
    @Binding var model: TripViewModel
    
    var body: some View {
        
        VStack(alignment: .leading) {
            
            TripImage(url: $model.imageUri)
                .overlay(alignment: .bottomLeading) {
                    Text("2 Activities")
                        .padding(6)
                        .background(
                            RoundedRectangle(cornerRadius: 20)
                                .fill(Color.gray)
                                
                        )
                        .padding()
                }
            
            Text(model.name)
                .font(.system(size: CGFloat(20)))
            
            HStack {
                Text("Subtitle")
                Spacer()
                Text("Creation date")
            }
            
        }
        .padding()
        
    }
    
}

struct TripImage: View {
    
    @Binding var url: String
    
    var body: some View {
        AsyncImage(url: URL(string: url)) { image in
            image.image?
                // Allow image to stretch in the parent view
                .resizable()
                // Scale to fit all available space in the parent and maintain its original aspect ratio
                //.scaledToFit()
                .aspectRatio(contentMode: .fit)
                .clipShape(.rect(cornerRadius: 20))
        }
    }
}

#Preview("Portrait Prev", traits: .portrait) {
    Sample3_TripUI().preferredColorScheme(.dark)
}

#Preview("Landscape Prev", traits: .landscapeLeft) {
    Sample3_TripUI().preferredColorScheme(.dark)
}
