import SwiftUI

/*
    A view implemented in SwiftUI
 */
struct MainContent: View {
    
    @State private var model = Sample2_ViewModel()
    
    var body: some View {
        ActivityList(model: model)
    }
}


struct ActivityList: View {
    
    @State var model: Sample2_ViewModel
    
    var body: some View {
        
        VStack {
            
            List {
                
                Section("My plants") {
                    ForEach($model.plantsList) { $plant in
                        ListItem(plant: plant)
                    }
                }
                
                Section("Other plants") {
                    ForEach($model.plantsList) { $plant in
                        ListItem(plant: plant)
                    }
                }
                
            }
            
            
            List($model.plantsList) { $plant in
                ListItem(plant: plant)
            }
            
            ActionButton(model: model)
            
        }
    }
}

struct ListItem: View {
    
    var plant: Plant
    
    var body: some View {
        HStack {
            
            Label(plant.name, systemImage: plant.icon)
            
            Spacer()
            
            Text(plant.text)
            
        }
    }
}

struct ActionButton: View {
    
    @State var model: Sample2_ViewModel
    
    var body: some View {
        Button("Action", systemImage: "trophy") {
            model.doAction()
        }
    }
}

struct ActivityListPreview : PreviewProvider {
    static var previews: some View {
        MainContent()
    }
}
