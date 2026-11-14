import Foundation
import SwiftData
import UIKit

@Model
class Moments {
    
    var title: String
    var note: String
    var imageData: Data?
    var timestamp: Date
    
    init(tile: String, note: String, imageData: Data? = nil, timestamp: Date = .now) {
        self.title = tile
        self.note = note
        self.imageData = imageData
        self.timestamp = timestamp
    }
    
    var image: UIImage? {
        imageData.flatMap {
            UIImage(data: $0)
        }
    }
}

extension Moments {
    
    static let shortSampleData = sampleData[0]
    static let longSampleData = sampleData[1]
    static let lastSampleData = sampleData[2]
    
    static let sampleData = [
        Moments(
            tile: "Moment 1",
            note: "Moment note 1",
            imageData: UIImage(named: "moment1")?.jpegData(compressionQuality: 0.8),
        ),
        Moments(
            tile: "🤟 Moment 2 🤟",
            note: "Moment note 2 Moment note 2 Moment note 2 Moment note 2 Moment note 2 Moment note 2 Moment note 2 Moment note 2 Moment note 2 Moment note 2 Moment note 2 Moment note 2 Moment note 2 Moment note 2 Moment note 2 Moment note 2 Moment note 2 Moment note 2 ",
            imageData: UIImage(named: "moment2")?.jpegData(compressionQuality: 0.8),
        ),
        Moments(
            tile: "🏄🏽‍♀️ Moment 3 🏄🏽‍♀️",
            note: "Moment note 3",
            imageData: UIImage(named: "moment3")?.jpegData(compressionQuality: 0.8),
        ),
    ]
    
}


