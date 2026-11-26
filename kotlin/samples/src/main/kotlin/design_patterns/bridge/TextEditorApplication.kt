package design_patterns.bridge

import design_patterns.bridge.impl.ApplicationRepository
import design_patterns.bridge.impl.storages.RemoteStorage

/**
 * Bridge pattern
 *
 * Sample program: Text editor application.
 *
 * Description:
 *
 * Let's say that we have complex text editor application.
 * We want to save our editor settings, extension settings and files in Database.
 *
 * We can create a class for doing this. However, what if later we need to save this information
 * in other Database or event in Remote Database. We are obliged to add new classes with different interfaces.
 * This is not quite flexible solution.
 *
 * Bridge pattern allows to solve this problem easy.
 *
 * We can create 2 parallel hierarchy for implementation and for abstraction of Databases.
 * Then we can reuse interfaces and implementation.
 *
 * Definition: Decouple the functional abstraction from the implementation so that the two can vary independently
 */
fun main() {

    // 1. Create our application classes
    val applicationRepository = ApplicationRepository()
    // a lot of other classes

    // 2. Working with our application ...

    // 3. It's time to save data

    // By default saving into local storage
    applicationRepository.saveData()

    // 4. User asks to save data to remote storage
    applicationRepository.setMainStorage(RemoteStorage())
    applicationRepository.saveData()

    // Now you can see that can easy work with different storages using single interface.
    // This can grow and be modified. Interfaces can be extended and new database
    // impls could be added.
}
