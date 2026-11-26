package design_patterns.bridge.impl

import design_patterns.bridge.impl.storages.ApplicationStorage
import design_patterns.bridge.impl.storages.LocalDatabaseStorage

class ApplicationRepository : BaseRepository(LocalDatabaseStorage()) {

    fun setMainStorage(storageRepo: ApplicationStorage) {
        storageRepoImpl = storageRepo
    }

    override fun saveData() {
        println("saveData(), Save application data to $storageRepoImpl")
        super.saveData()
        println("saveData(), Data is saved")
    }

}