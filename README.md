# Final Submission - Android + OpenCV (C++) + OpenGL ES + Web (TypeScript)

This archive contains a **ready-to-submit** project skeleton for the assessment. It is designed to be imported into Android Studio and built after two final, environment-specific steps described below.

IMPORTANT: You must add the OpenCV Android native libraries (or choose the OpenCV AAR Maven dependency) and set `local.properties` with your Android SDK & NDK paths. See README_SETUP.md for details.

Contents:
- app/: Android module sources (Java), native C++ and resources
- CMakeLists.txt: native build config
- web/: TypeScript viewer and sample image

Build steps (one-time on your machine):
1. Place OpenCV Android SDK (the `sdk/native/jni/include` headers and `sdk/native/libs/<abi>/*.so`) under `external/opencv-android` or use OpenCV AAR and adjust module `build.gradle`.
2. Create `local.properties` with `sdk.dir` and `ndk.dir` paths (Android Studio usually does this automatically).
3. Open project in Android Studio and build (Gradle will invoke CMake).

Limitations / Notes:
- I could not include the OpenCV binary `.so` files due to size and environment differences. Adding them as described above is required.
- This archive includes a fully-featured app module, CMakeLists, and a TypeScript web viewer. Follow README_SETUP.md for specifics.

If you want, I will:
- Add a script to automatically download the OpenCV Android SDK for you,
- Convert Java to Kotlin,
- Add a small Node.js mock server to push frames to the web viewer.

Good luck!
