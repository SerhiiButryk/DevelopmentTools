# Build tool

1. Run test

./gradlew --console plain connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.class#test_example

2. App build & install tasks
./gradlew installDebug

./gradlew assembleDebug 
./gradlew assembleRelease

With flags
./gradlew -PAPP_ABI=armeabi-v7a app:assembleDebug

3. Execute single build script
./gradlew -b /path/to/some_script.gradle someTask

4. Dependency Insight
./gradlew -q dependencyInsight --dependency artifactGroupName

5. Gradle dependencies check

./gradlew -q app:dependencies
./gradlew app:resolvableConfigurations
