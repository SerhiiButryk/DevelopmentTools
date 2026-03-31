//
//  ExampleSwiftUI.swift
//  IOSTestApp
//
//  Created by Serhii on 31.03.2026.
//

import SwiftUI

struct ActivitiesView: View {
    
    private let activitiyList = ["Archery", "Baseball", "Basketball"]
    private let colorList: [Color] = [.blue, .cyan, .green]
    
    // UI state variables. Swift UI updates automatically
    // when its value changes.
    @State private var currActivity = "Archery"
    @State private var currColor: Color = .blue
    
    @State private var index = 0
    
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
                
                index += 1
                if index >= colorList.count {
                    index = 0
                }
                
                withAnimation {
                    currColor = colorList[index]
                    currActivity = activitiyList[index]
                }
                
            }.padding()
            .buttonStyle(.borderedProminent)
            
        }
    }
    
}

struct Preview : PreviewProvider {
    static var previews: some View {
        ActivitiesView()
    }
}
