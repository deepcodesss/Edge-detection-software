Android + OpenCV (C++) + OpenGL ES + Web (TypeScript)

Contents:
- app/: Android module sources (Java), native C++ and resources
- CMakeLists.txt: native build config
- web/: TypeScript viewer and sample image

Build steps (one-time on your machine):
1. Place OpenCV Android SDK (the `sdk/native/jni/include` headers and `sdk/native/libs/<abi>/*.so`) under `external/opencv-android` or use OpenCV AAR and adjust module `build.gradle`.
2. Create `local.properties` with `sdk.dir` and `ndk.dir` paths (Android Studio usually does this automatically).
3. Open project in Android Studio and build (Gradle will invoke CMake).
